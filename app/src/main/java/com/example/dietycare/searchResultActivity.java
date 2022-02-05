package com.example.dietycare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class searchResultActivity extends AppCompatActivity {
    private searchResultAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView searchBox;
    private ImageButton backToMealPage;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        goBackToMealPage();

        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");
        searchBox = findViewById(R.id.search_box);
        searchBox.setText(keyword);

        AlgoliaSearch(keyword);

        searchAgain();
    }

    private void goBackToMealPage () {
        backToMealPage = findViewById(R.id.backToMealPage);
        backToMealPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchResultActivity.this, MealRecommendationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AlgoliaSearch(String keyword) {
        Client client = new Client("P2LIVS5AHL", "6688c266356e95b6b38894d6d9e80487");
        Index index = client.getIndex("Food Data");
        ArrayList<Food> tempArr = new ArrayList<>();
        CompletionHandler compHand = new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                try {
                    assert jsonObject != null;
                    JSONArray arr = jsonObject.getJSONArray("hits");
//                    Iterator<String> jsonKeys = jsonObject.keys();
//                    while (jsonKeys.hasNext()) {
//                        System.out.println(jsonKeys.next().toString());
//                    }
                    for (int i = 0; i < arr.length(); i++) {
                        String foodName = arr.getJSONObject(i).get("Food").toString();
                        double protein = Double.parseDouble(arr.getJSONObject(i).get("Pro_100G").toString());
                        double fat = Double.parseDouble(arr.getJSONObject(i).get("Fat_100G").toString());
                        double carbo = Double.parseDouble(arr.getJSONObject(i).get("CHO_100G").toString());
                        double calorie = Double.parseDouble(arr.getJSONObject(i).get("Cal_100G").toString());
                        tempArr.add(new Food(foodName, protein, fat, carbo, calorie));
                    }
                    //update UI
                    recycler = findViewById(R.id.recycleView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchResultActivity.this);
                    recycler.setLayoutManager(linearLayoutManager);
                    searchResultAdapter adapter = new searchResultAdapter(tempArr, searchResultActivity.this);
                    recycler.setAdapter(adapter);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        };
        index.searchAsync(new Query(keyword).setHitsPerPage(50), compHand);
    }

    private void searchAgain() {
        searchBox = findViewById(R.id.search_box);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = searchBox.getText().toString();
                    AlgoliaSearch(keyword);
                    InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return true;
            }
        });
    }
}