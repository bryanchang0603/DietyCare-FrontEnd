package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    private double intakeCal;
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
    private char gender;
    private String bodyType;
    private int birthYear;
    private int birthMonth;
    private int birthDate;
    private int age;
    private String outputStr;
    private newHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
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
                                            Intent intent = new Intent(MainActivity.this, mealRcmdActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(MainActivity.this, shareActivity.class);
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

        main_page_event();

    }

    public void main_page_event(){

        String path = getApplicationContext().getFilesDir().toString()+"/personInfo.txt";
        File file = new File(path);
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
        } catch (FileNotFoundException e) {
            display(0,0,0,0,0,
                    0,0,0);
        }

        handler = new newHandler();

        final RadioGroup radGroup= findViewById(R.id.radio_group);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int id) {
                RadioButton rb = findViewById(id);
                dietGoalIs = rb.getText().toString();
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

                if (dietGoalIs.equals("build muscles")) {

                    intakeCal = 11.1;

                    consumedProtein = 49.5;
                    consumedFat = 32.1;
                    consumedCarbo = 66;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&exercise_level=%d&body_fat=%.2f&body_type=%s",
                                    age, weight, height, gender,5,bodyFat,bodyType);
                            String url = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/muscleIntakeCustomization?"+temp;

                            String output = requestData(url, "GET");
                            Message msg = handler.obtainMessage();
                            msg.obj = output;
                            handler.sendMessage(msg);
                            System.out.println(output);
                        }
                    }).start();

                } else if (dietGoalIs.equals("lose weight")) {
                    intakeCal = 11.1;

                    consumedProtein = 49.5;
                    consumedFat = 32.1;
                    consumedCarbo = 66;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                                    age, weight, height, gender,targetWeight,bodyFat);
                            String url = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?"+temp;

                            String output = requestData(url, "GET");
                            Message msg = handler.obtainMessage();
                            msg.obj = output;
                            handler.sendMessage(msg);
                            System.out.println(height);
                        }
                    }).start();

                } else if (dietGoalIs.equals("remain shape")) {
                    intakeCal = 11.1;

                    consumedProtein = 49.5;
                    consumedFat = 32.1;
                    consumedCarbo = 66;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String temp = String.format("age=%d&weight=%.1f&height=%.1f&gender=%s&target_weight=%.1f&body_fat=%.2f",
                                    age, weight, height, gender,weight,bodyFat);
                            String url = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/weightIntakeCustomization?"+temp;

                            String output = requestData(url, "GET");
                            Message msg = handler.obtainMessage();
                            msg.obj = output;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }

    private void display(double suggestedCal, double intakeCal, double consumedProtein,
                         double consumedFat, double consumedCarbo, double suggestedProtein,
                         double suggestedFat, double suggestedCarbo) {
        TextView textValSuggested = findViewById(R.id.text_val_suggested);
        TextView textValLeft = findViewById(R.id.text_val_left);
        TextView textValIntake = findViewById(R.id.text_val_intake);
        TextView textValProtein = findViewById(R.id.text_val_protein);
        TextView textValFat = findViewById(R.id.text_val_fat);
        TextView textValCarbo = findViewById(R.id.text_val_carbo);
        leftCal = suggestedCal - intakeCal;
        textValSuggested.setText(String.format("%.1f",suggestedCal));
        textValIntake.setText(String.format("%.1f",intakeCal));
        textValLeft.setText(String.format("%.1f",leftCal));
        textValProtein.setText(String.format("%.1f",consumedProtein)+'/'+String.format("%.1f",suggestedProtein));
        textValFat.setText(String.format("%.1f",consumedFat)+'/'+String.format("%.1f",suggestedFat));
        textValCarbo.setText(String.format("%.1f",consumedCarbo)+'/'+String.format("%.1f",suggestedCarbo));
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
                suggestedCal = Double.parseDouble(jobj.get("suggest_cal").toString());
                suggestedProtein = Double.parseDouble(jobj.get("suggest_pro").toString());
                suggestedFat = Double.parseDouble(jobj.get("suggest_fat").toString());
                suggestedCarbo = Double.parseDouble(jobj.get("suggest_ch").toString());
                leftCal = suggestedCal - intakeCal;
                display(suggestedCal, intakeCal, consumedProtein, consumedFat, consumedCarbo,
                        suggestedProtein, suggestedFat, suggestedCarbo);

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