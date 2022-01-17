package com.ui.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
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
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
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
            bodyFat = Double.parseDouble(personInfo.get("Body_Fat"));
        } catch (FileNotFoundException e) {
            display(0,0,0,0,0,
                    0,0,0);
        }

        final RadioGroup radGroup= findViewById(R.id.radio_group);

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
        textValProtein.setText(Double.toString(consumedProtein)+'/'+Double.toString(suggestedProtein));
        textValFat.setText(Double.toString(consumedFat)+'/'+Double.toString(suggestedFat));
        textValCarbo.setText(Double.toString(consumedCarbo)+'/'+Double.toString(suggestedCarbo));
    }

}