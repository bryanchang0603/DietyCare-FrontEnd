package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private ImageButton progress_btn, meal_btn, community_btn, account_btn;
    private String dietGoalIs;
    private double suggestedCal;
    private double consumedCal;
    private double leftCal;
    private double consumedProtein;
    private double consumedFat;
    private double consumedCarbo;
    private double suggestedProtein;
    private double suggestedFat;
    private double suggestedCarbo;
    private double targetWeight;
    private double height;
    private double bodyFat;
    private double weight;
    private int exerciseLevel;
    private char gender;
    private String bodyType;
    private int birthYear;
    private int birthMonth;
    private int birthDate;
    private int age;
    private String outputStr;
    private newHandler handler;

    //the following variables are for jump to Post page
    private Button post_btn;

    //Variables for saving radio buttons
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor SPEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // The following codes are for testing the UID
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        assert currentUser != null;
//        System.out.println(currentUser.getUid());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        menu_button_creation();
        main_page_event();

        // //the following variables are for jump to Post page
        // post_btn= findViewById(R.id.to_post);
        // post_btn.setOnClickListener(new View.OnClickListener() {
        //                                     @Override
        //                                     public void onClick(View view) {
        //                                         Intent intent = new Intent(MainActivity.this, postingActivity.class);
        //                                         //Starting of the Intent
        //                                         startActivity(intent);
        //                                         finish();
        //                                     }
        //                                 }
        // );


    }

    private void menu_button_creation(){
        progress_btn = findViewById(R.id.menu_btn_progress_main);
        meal_btn = findViewById(R.id.menu_btn_meal_main);
        community_btn = findViewById(R.id.menu_btn_community_main);
        account_btn = findViewById(R.id.main_btn_account_main);


        progress_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(MainActivity.this, progress.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MainActivity.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(MainActivity.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        account_btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(MainActivity.this, accountActivity.class);
                                               //Starting of the Intent
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );
    }

    public void main_page_event(){

        String path = getApplicationContext().getFilesDir().toString()+"/personInfo.txt";
        File file = new File(path);
        sharedPreferences = getSharedPreferences("userPref", 0);
        int mealGoalSP = sharedPreferences.getInt("mealGoalSP", 0); //default 0, no radio button will be selected
            // 1 means build build muscles, 2 means lose weight, 3 means remain shape
        SPEditor = sharedPreferences.edit();
        handler = new newHandler();
        HashMap<String, String> personInfo = new HashMap<String, String>();


        try{
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] temp = data.split(" ");
                personInfo.put(temp[0], temp[1]);
            }
            height = Double.parseDouble(personInfo.get("Height"));
            weight = Double.parseDouble(personInfo.get("Weight"));
            targetWeight = Double.parseDouble(personInfo.get("Target_Weight"));
            bodyFat = Double.parseDouble(personInfo.get("Body_Fat"))/100;
            birthYear = Integer.parseInt(personInfo.get("Year"));
            birthMonth = Integer.parseInt(personInfo.get("Month"));
            birthDate = Integer.parseInt(personInfo.get("Day"));
            gender = personInfo.get("Sex").charAt(0);
            bodyType = personInfo.get("Body_Type").toLowerCase();
            exerciseLevel = Integer.parseInt(personInfo.get("Exercise"));
        } catch (FileNotFoundException e) {
            displaySuggested(0,0,0,0);
            displayConsumedAndLeftCal(0,0,0,0,0);
        }

        // calculate age
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH)+1;
        int currentDate = cal.get(Calendar.DATE);
        age = currentYear - birthYear;

        if (currentMonth < birthMonth) {
            age--;
        } else if (currentMonth == birthMonth) {
            if (currentDate < birthDate) {
                age--;
            }
        }


        // code for setting the radio buttons' saved status
        RadioButton rbBuild, rbLose, rbRemain;
        String customParam, consumedInfo, urlParamTarget, urlConsumed;
        rbBuild = findViewById(R.id.radioButton_muscle);
        rbLose = findViewById(R.id.radioButton_weight);
        rbRemain = findViewById(R.id.radioButton_shape);

        int userID = 123;
        //String intakeDate = String.format("%d-%02d-%02d", currentYear, currentMonth, currentDate);
        String intakeDate = "2022-02-03";
        consumedInfo = String.format("user_id=%d&intake_date=%s",userID, intakeDate);
        urlConsumed = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/dailyNutrient?" + consumedInfo;

        switch(mealGoalSP){
            case 1:
                rbBuild.setChecked(true);
                customParam = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&exercise_level=%d&body_fat=%.2f&body_type=%s",
                        age, weight, height, gender, exerciseLevel, bodyFat, bodyType);
                urlParamTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/muscleIntakeCustomization?" + customParam;
                set_diet_goal(handler, SPEditor,1, urlConsumed, urlParamTarget);
                break;
            case 2:
                rbLose.setChecked(true);
                customParam = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                        age, weight, height, gender, targetWeight, bodyFat);
                urlParamTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?" + customParam;
                set_diet_goal(handler, SPEditor, 2, urlConsumed, urlParamTarget);
                break;
            case 3:
                rbRemain.setChecked(true);
                customParam = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                        age, weight, height, gender, weight, bodyFat);
                urlParamTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?" + customParam;
                set_diet_goal(handler, SPEditor, 3, urlConsumed,urlParamTarget);
                break;
                default:
                    rbBuild.setChecked(false);
                    rbLose.setChecked(false);
                    rbRemain.setChecked(false);

        }

        // radio button saving status end

        final RadioGroup radGroup= findViewById(R.id.radio_group);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int id) {
                RadioButton rb = findViewById(id);
                String temp, urlTarget, consumedInfo, urlConsumed;
                dietGoalIs = rb.getText().toString();

                int userID = 123;
                String intakeDate = "2022-02-03";

                consumedInfo = String.format("user_id=%d&intake_date=%s",userID, intakeDate);
                urlConsumed = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/dailyNutrient?" + consumedInfo;

                switch (dietGoalIs) {
                    case "build muscles":
                        temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&exercise_level=%d&body_fat=%.2f&body_type=%s",
                                age, weight, height, gender, exerciseLevel, bodyFat, bodyType);
                        urlTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/muscleIntakeCustomization?" + temp;
                        set_diet_goal(handler, SPEditor, 1, urlConsumed, urlTarget);
                        break;
                    case "lose weight":
                        temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                                age, weight, height, gender, targetWeight, bodyFat);
                        urlTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?" + temp;
                        set_diet_goal(handler, SPEditor, 2, urlConsumed, urlTarget);
                        break;
                    case "remain shape":
                        temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                                age, weight, height, gender, weight, bodyFat);
                        urlTarget = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?" + temp;
                        set_diet_goal(handler, SPEditor, 3, urlConsumed, urlTarget);
                        break;
                }
                SPEditor.commit();
            }
        });
    }

    private void set_diet_goal(newHandler handler, SharedPreferences.Editor SPEditor, int mealGoalSP, String paramURLConsumed, String paramURLTarget){

        SPEditor.putInt("mealGoalSP", mealGoalSP);
        newHandler2 handler2 = new newHandler2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String output = requestData(paramURLConsumed, "GET");
                Message msg = handler2.obtainMessage();
                msg.obj = output;
                handler2.sendMessage(msg);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String output = requestData(paramURLTarget, "GET");
                Message msg = handler.obtainMessage();
                msg.obj = output;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void displaySuggested(double suggestedProtein, double suggestedFat,
                                  double suggestedCarbo, double suggestedCal) {
        TextView textValSuggested = findViewById(R.id.text_val_suggested);

        TextView textSuggestedProtein = findViewById(R.id.text_val_protein);
        TextView textSuggestedFat = findViewById(R.id.text_val_fat);
        TextView textSuggestedCarbo = findViewById(R.id.text_val_carbo);

        textValSuggested.setText(String.format("%.1f",suggestedCal));
//        textValIntake.setText(String.format("%.1f",intakeCal));
//        textValLeft.setText(String.format("%.1f",leftCal));
        textSuggestedProtein.setText(String.format("%.1f",suggestedProtein));
        textSuggestedFat.setText(String.format("%.1f",suggestedFat));
        textSuggestedCarbo.setText(String.format("%.1f",suggestedCarbo));
    }

    private void displayConsumedAndLeftCal(double consumedProtein, double consumedFat, double consumedCarbo,
                                           double consumedCal, double leftCal) {
        TextView textConsumedProtein = findViewById(R.id.text_consumed_protein);
        TextView textConsumedFat = findViewById(R.id.text_consumed_fat);
        TextView textConsumedCarbo = findViewById(R.id.text_consumed_carbo);
        TextView textConsumedCal = findViewById(R.id.text_val_intake);
        TextView textCalLeft = findViewById(R.id.text_val_left);
        textConsumedProtein.setText(String.format("%.1f",consumedProtein));
        textConsumedFat.setText(String.format("%.1f",consumedFat));
        textConsumedCarbo.setText(String.format("%.1f",consumedCarbo));
        textConsumedCal.setText(String.format("%.1f", consumedCal));
        textCalLeft.setText(String.format("%.1f", leftCal));
    }

    public String requestData(String inputURL, String reqMethod) {
        HttpURLConnection connection;
        InputStream in;
        InputStreamReader inReader;
        BufferedReader br;
        try {
            URL url = new URL(inputURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(reqMethod);

            connection.connect();
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);

            in = connection.getInputStream();
            inReader = new InputStreamReader(in);
            br = new BufferedReader(inReader);
            String s;
            StringBuffer strBuf = new StringBuffer();
            while((s = br.readLine()) != null) {
                strBuf.append(s);
            }
            connection.disconnect();
            return strBuf.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "None";
    }

    class newHandler extends Handler {
        public String outputStr;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            outputStr = msg.obj.toString();

            try {
                JSONObject jobj = new JSONObject(outputStr);
                suggestedProtein = Double.parseDouble(jobj.get("suggest_pro").toString());
                suggestedFat = Double.parseDouble(jobj.get("suggest_fat").toString());
                suggestedCarbo = Double.parseDouble(jobj.get("suggest_ch").toString());
                suggestedCal = Double.parseDouble(jobj.get("suggest_cal").toString());
                displaySuggested(suggestedProtein, suggestedFat, suggestedCarbo, suggestedCal);
                File file = new File(getFilesDir()+"/suggestion.txt");
                if (!file.exists()){
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(String.format("suggestedCal:%.2f\nsuggestedProtein:%.2f\n" +
                                "suggestedFat:%.2f\nsuggestedCarbo:%.2f", suggestedCal, suggestedProtein,
                        suggestedFat, suggestedCarbo));
                fileWriter.close();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    class newHandler2 extends Handler {
        public String outputStr;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            outputStr = msg.obj.toString();

            try {
                JSONObject jobj = new JSONObject(outputStr);
                consumedCal = Double.parseDouble(jobj.get("cal_consumed").toString());
                consumedProtein = Double.parseDouble(jobj.get("pro_consumed").toString());
                consumedFat = Double.parseDouble(jobj.get("fat_consumed").toString());
                consumedCarbo = Double.parseDouble(jobj.get("CHO_consumed").toString());
                leftCal = Double.parseDouble(jobj.get("cal_left").toString());
                displayConsumedAndLeftCal(consumedProtein,consumedFat, consumedCarbo, consumedCal, leftCal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseAuth.getInstance().signOut(); // this is for login test purpose need to remove in integration
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "user not login", Toast.LENGTH_SHORT).show();
            sendToLogin();
        }

    }


    private void sendToLogin() {

        //Declaration of explict Intent from MainActivity to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        //Starting of the Intent
        startActivity(intent);
        finish();
    }

}