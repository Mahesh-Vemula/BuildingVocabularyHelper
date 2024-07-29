package com.mv.buildingVocabularyHelper.business;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mv.buildingVocabularyHelper.dto.Collegiate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DictonaryAPIManager {

    private Gson gson = new Gson();

    public Boolean updateHistoryData(SharedPreferences preferences, Collegiate collegiate){
        List<Collegiate> historyObjects = getCollegiatesFromPreferences(preferences);
        String historyData;
        SharedPreferences.Editor editor = preferences.edit();
        Boolean isAdded = addOnlyNewWordToHistory(historyObjects, collegiate);
        historyData =  gson.toJson(historyObjects);
        editor.putString("historyWords", historyData);
        editor.apply();
        return isAdded;
    }

    public List<Collegiate> getCollegiatesFromPreferences(SharedPreferences preferences) {
        String historyData = preferences.getString("historyWords", null);
        List<Collegiate> historyObjects;
        if(historyData != null) {
            Log.v("API call", historyData);
            Type listType = new TypeToken<List<Collegiate>>() {}.getType();
            historyObjects = gson.fromJson(historyData, listType);
            Log.v("API call", historyObjects.toString());
        }else{
            historyObjects = new ArrayList<Collegiate>();
        }
        return historyObjects;
    }

    private Boolean addOnlyNewWordToHistory(List<Collegiate> historyObjects, Collegiate collegiate) {
        Boolean isAdded;
        Collegiate searchCollegiate = historyObjects.stream()
                .filter(collegiate1 -> collegiate.getMeta().getId().equals(collegiate1.getMeta().getId()))
                .findFirst()
                .orElse(null);
        if(searchCollegiate != null){
            Log.v(" Word exist in history: ", searchCollegiate.toString());
            isAdded = false;
        }else{
            historyObjects.add(collegiate);
            Log.v(" Word added in history: ", collegiate.toString());
            isAdded = true;
        }


        return isAdded;
    }

}
