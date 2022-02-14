package com.example.dietycare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class MealRecommendationActivity extends AppCompatActivity {

    private TextView meal_info_tv;
    private TextView searchBox;
    private Button regenerate_btw;
    private Spinner meal_selection_sp;
    private String meal_selection = "breakfast";
    private JSONObject meal_plan;
    private ImageButton home_btn, meal_btn, community_btn, account_btn, progress_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recommendation);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Block to make strict mode to allow
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        HashMap<String, String> suggestion_nutrients = readSuggestionNutrients();
        Integer target_calories = (int) Double.parseDouble(suggestion_nutrients.get("suggestedCal"));
        try {
            meal_plan = getMealRecommendation(target_calories);
            String meal_detail = extractMealPlan(meal_plan, meal_selection);
            set_meal_plan_tv(meal_detail);
        } catch (Exception e) {
            e.printStackTrace();
            set_meal_plan_tv("Something went wrong.");
        }

        uploadPhoto(this);

        regenerate_btw = (Button) findViewById(R.id.regenerate);
        regenerate_btw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    meal_plan = getMealRecommendation(target_calories);
                    String meal_detail = extractMealPlan(meal_plan, meal_selection);
                    set_meal_plan_tv(meal_detail);
                } catch (Exception e) {
                    e.printStackTrace();
                    set_meal_plan_tv("Something went wrong.");
                }
            }
        });

        // Spiner
        String[] bodyType = {"Breakfast", "Lunch", "Dinner"};
        meal_selection_sp = (Spinner) findViewById(R.id.mealSelection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bodyType);
        meal_selection_sp.setAdapter(adapter);
        meal_selection_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meal_selection = (String) parent.getItemAtPosition(position);
                meal_selection = meal_selection.toLowerCase();
                try {
                    String meal_detail = extractMealPlan(meal_plan, meal_selection);
                    set_meal_plan_tv(meal_detail);
                } catch (Exception e) {
                    e.printStackTrace();
                    set_meal_plan_tv("Something went wrong.");
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Algolia Seach
        //AlgoliaSearch();

        // setup the menu buttons
        home_btn = findViewById(R.id.menu_btn_home_recmd);
        community_btn = findViewById(R.id.menu_btn_community_recmd);
        account_btn = findViewById(R.id.main_btn_account_recmd);
        progress_btn = findViewById(R.id.menu_btn_progress_recmd);

        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MealRecommendationActivity.this, MainActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        progress_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(MealRecommendationActivity.this, progress.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(MealRecommendationActivity.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        account_btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(MealRecommendationActivity.this, accountActivity.class);
                                               //Starting of the Intent
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );

        search();
    }

    private void set_meal_plan_tv(String meal_detail) {
        meal_info_tv = (TextView) findViewById(R.id.mealInfo);
        meal_info_tv.setText(meal_detail);
    }

    private JSONObject getMealRecommendation(Integer calories) throws Exception{
        // Create a neat value object to hold the URL
        String str_url = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/mealRecommendation?calories=" + calories.toString();
        URL url = new URL(str_url) ;
        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(responseStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String response = buf.toString("UTF-8");

        final JSONObject obj = new JSONObject(response);

        return obj;


    }

    private String extractMealPlan(JSONObject meal_plan, String meal_selection) throws Exception{
        // Note: variable meal_selection is a String and it can only be one of the following: breakfast, lunch, dinner

        final JSONObject menus = meal_plan.getJSONObject("menus");
        final JSONObject breakfast = menus.getJSONObject(meal_selection);

        final JSONObject br_nutrient = breakfast.getJSONObject("nutrient");
        Double calories = br_nutrient.getDouble("Kcal");
        Double carb = br_nutrient.getDouble("carb");
        Double fat = br_nutrient.getDouble("fat");
        Double protein = br_nutrient.getDouble("protein");
        // String nutrient_list_string = "Calories: "+calories.toString()+" Kcal\n"；
        String nutrient_list_string = String.format("Calories: %.2f\nProtein: %.2f g\nCarbon Hydrate: %.2f g\nFat: %.2f g\n", calories, protein, carb, fat);
        //System.out.println(nutrient_list_string);

        final JSONArray br_dishes = breakfast.getJSONArray("dishes");
        final int n = br_dishes.length();

        String dish_list_str = "";
        for (int i = 0; i < n; ++i) {
            final JSONObject dish = br_dishes.getJSONObject(i).getJSONObject("dish");
            String dish_name = dish.getString("name");
            Double dish_amount = dish.getDouble("amount");
            dish_list_str += dish_name + "  " + dish_amount.toString() + " g\n";
        }

        return "Food Name: \n"+dish_list_str + "\nNutrients: \n" + nutrient_list_string;
    }

    private HashMap<String, String> readSuggestionNutrients(){
        File path = getApplicationContext().getFilesDir();
        File read = new File(path, "suggestion.txt");
        HashMap<String, String> readInfo = new HashMap<String, String>();
        try{
            Scanner reader = new Scanner(read);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] temp = data.split(":");
                readInfo.put(temp[0], temp[1]);
            }
        }catch(Exception e){

        }
        return readInfo;
    }

    private void uploadPhoto(Context context) {
        ImageButton imgButton = findViewById(R.id.uploadPhoto);
        ActivityResultLauncher arl = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Intent intent = new Intent(context, ImageRecognitionActivity.class);
                        intent.setData(result);
                        startActivity(intent);
                    }
                });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch("image/*");
            }
        });
    }

    private void goToSearchResultPage(String keyword) {
        Intent intent = new Intent(MealRecommendationActivity.this, searchResultActivity.class);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
        finish();
    }

    private void search() {
        searchBox = findViewById(R.id.keyword);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = searchBox.getText().toString();
                    goToSearchResultPage(keyword);
                }
                return true;
            }
        });
    }
}


