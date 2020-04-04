package com.mv.buildingVocabularyHelper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mv.buildingVocabularyHelper.api.DictornaryAPI;
import com.mv.buildingVocabularyHelper.business.DictonoryAPIManager;
import com.mv.buildingVocabularyHelper.dto.Collegiate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ListView listView;
    private TextView displayText;
    private Button button;
    private ArrayAdapter adapter;
    private List<Collegiate> collegiates;
    DictonoryAPIManager dictonoryAPIManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        displayText = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    searchButtonClick(v);
                }
                return false;
            }
        });
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        dictonoryAPIManager = new DictonoryAPIManager();
        collegiates = dictonoryAPIManager.getCollegiatesFromPreferences(preferences);

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, collegiates) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setTypeface(null, Typeface.BOLD);

                text1.setText(collegiates.get(position).getMeta().getId());
                String[] definations = collegiates.get(position).getShortdef();
                String combinedDefinations = String.join("\n\n", definations);
                combinedDefinations = combinedDefinations + "\n\n";
                text2.setText(combinedDefinations);
                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addnew){
            listView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            displayText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            Toast.makeText(this, "You clicked add new", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.history){
            listView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            displayText.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            Toast.makeText(this, "You clicked history", Toast.LENGTH_SHORT).show();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }



    public void searchButtonClick(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String searchWord = editText.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.dictionaryapi.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DictornaryAPI dictornaryAPI = retrofit.create(DictornaryAPI.class);


        Call<List<Collegiate>> call = dictornaryAPI.getDefinations(searchWord);

        call.enqueue(new Callback<List<Collegiate>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Collegiate>> call, Response<List<Collegiate>> response) {
                Log.v("API call", "testing log");
                List<Collegiate> collegiatesLocal = response.body();
                if(collegiatesLocal.size()>0){
                    String[] definations = collegiatesLocal.get(0).getShortdef();
                    String combinedDefinations = String.join("\n\n", definations);
                    Log.v("API call", combinedDefinations);
                    displayText.setText(combinedDefinations);


                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                    Boolean added = dictonoryAPIManager.updateHistoryData(preferences, collegiatesLocal.get(0));
                    if(added){
                        collegiates.add(collegiatesLocal.get(0));
                        adapter.notifyDataSetChanged();
                    }
                }

                Log.v("API call", String.valueOf(collegiatesLocal.size()));
            }

            @Override
            public void onFailure(Call<List<Collegiate>> call, Throwable t) {
                Log.v("API call", "Call failed");
                displayText.setText("No meaning found for given word. Check spelling and try again");
            }
        });


        displayText.setText("loading meaning...");
    }

    class MyAdapter extends BaseAdapter{

        private Context context;
        private List<Collegiate> collegiates;

        public MyAdapter(Context context, List<Collegiate> collegiates){
            this.context = context;
            this.collegiates = collegiates;
        }

        @Override
        public int getCount() {
            return collegiates.size();
        }

        @Override
        public Object getItem(int i) {
            return collegiates.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            return null;
        }
    }


}
