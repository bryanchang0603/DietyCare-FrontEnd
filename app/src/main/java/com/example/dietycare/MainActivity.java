package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

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

    public void main_page_event() {

        String path = getApplicationContext().getFilesDir().toString() + "/personInfo.txt";
        File file = new File(path);
        HashMap<String, String> personInfo = new HashMap<String, String>();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] temp = data.split(" ");
                personInfo.put(temp[0], temp[1]);
            }
            height = Double.parseDouble(personInfo.get("Height"));
            weight = Double.parseDouble(personInfo.get("Weight"));
            targetWeight = Double.parseDouble(personInfo.get("Target_Weight"));
            bodyFat = Double.parseDouble(personInfo.get("Body_Fat"));
        } catch (FileNotFoundException e) {
            display(0, 0, 0, 0, 0,
                    0, 0, 0);
        }

        final RadioGroup radGroup = findViewById(R.id.radio_group);

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int id) {
                RadioButton rb = findViewById(id);
                dietGoalIs = rb.getText().toString();
                if (dietGoalIs.equals("build muscles")) {
                    suggestedCal = 30;
                    intakeCal = 11.1;

                    consumedProtein = 49.5;
                    consumedFat = 32.1;
                    consumedCarbo = 66;

                    suggestedProtein = 100;
                    suggestedFat = 50;
                    suggestedCarbo = 80;

                } else if (dietGoalIs.equals("lose weight")) {
                    suggestedCal = 10;
                    intakeCal = 5.5;

                    consumedProtein = 9;
                    consumedFat = 11;
                    consumedCarbo = 12;

                    suggestedProtein = 120;
                    suggestedFat = 30;
                    suggestedCarbo = 77;

                } else if (dietGoalIs.equals("remain shape")) {
                    suggestedCal = 30;
                    intakeCal = 10;

                    consumedProtein = 15;
                    consumedFat = 44;
                    consumedCarbo = 32.9;

                    suggestedProtein = 87;
                    suggestedFat = 55;
                    suggestedCarbo = 99.9;
                }
                display(suggestedCal, intakeCal, consumedProtein, consumedFat, consumedCarbo,
                        suggestedProtein, suggestedFat, suggestedCarbo);
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
        textValSuggested.setText(Double.toString(suggestedCal));
        textValIntake.setText(Double.toString(intakeCal));
        textValLeft.setText(Double.toString(leftCal));
        textValProtein.setText(Double.toString(consumedProtein) + '/' + Double.toString(suggestedProtein));
        textValFat.setText(Double.toString(consumedFat) + '/' + Double.toString(suggestedFat));
        textValCarbo.setText(Double.toString(consumedCarbo) + '/' + Double.toString(suggestedCarbo));
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