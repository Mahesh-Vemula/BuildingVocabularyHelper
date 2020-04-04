package com.mv.buildingVocabularyHelper.api;

import com.mv.buildingVocabularyHelper.dto.Collegiate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictornaryAPI {

    @GET("references/collegiate/json/{word}?key=28e3909a-ad40-4c90-97d8-d98c29889392")
    Call<List<Collegiate>> getDefinations(@Path("word") String wordToFetchDefination);

}
