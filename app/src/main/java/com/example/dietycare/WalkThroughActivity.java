package com.example.dietycare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WalkThroughActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private TextView textGet;
    private String bodyT, exerciseL, DGoal;
    private int BYear, BMonth, BDay;
    private String Gender = "Male";
    private String checkVeg = "False";

    String[] bodyType = {"Ectomorph", "Endomorph", "Mesomorph"};
    String[] exercise_level = {"1", "2", "3", "4", "5"};
    String[] diet_goal = {"Build Muscle", "Lose Weight", "Remain Shape"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //Get user id from register
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String userID = firebaseUser.getUid();

        //Initialized all elements on layout
        Spinner sp = (Spinner) findViewById(R.id.spinner3);
        Spinner exer_sp = (Spinner) findViewById(R.id.spinner_exercise);
        Spinner dg_sp = (Spinner) findViewById(R.id.DGspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bodyType);
        sp.setAdapter(adapter);

        ArrayAdapter<String> exer_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, exercise_level);
        exer_sp.setAdapter(exer_adapter);

        ArrayAdapter<String> dg_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, diet_goal);
        dg_sp.setAdapter(dg_adapter);

        EditText weight = findViewById(R.id.editTextNumber5);
        EditText BFat= findViewById(R.id.editTextNumber6);
        EditText Tweight = findViewById(R.id.editTextNumber7);
        EditText height = findViewById(R.id.editTextNumber8);
        textGet = findViewById(R.id.textView);
        RadioButton Yes = findViewById(R.id.isVeg);
        RadioButton No = findViewById(R.id.notVeg);
        RadioButton maleB = findViewById(R.id.radioMale);
        RadioButton femaleB = findViewById(R.id.radioFemale);
        ImageButton btn_Cal = findViewById(R.id.btncal);
        Button btn_continue = findViewById(R.id.continueBtn);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyT = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        exer_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exerciseL = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dg_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DGoal = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_continue.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int age = 2022 - BYear;
                float body_fat = Float.parseFloat("" + BFat.getText());
                body_fat = body_fat / 100;
                String baseUrl = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com";
                String path = "/user";
                String payload = "{\"user_id\": " + "\"" + userID + "\", "
                        + "\"gender\": " + "\"" + Gender + "\", "
                        + "\"age\": " + "\"" + age + "\", "
                        + "\"height\": " + "\"" + height.getText() + "\", "
                        + "\"weight\": " + "\"" + weight.getText() + "\", "
                        + "\"target_weight\": " + "\"" + Tweight.getText() + "\", "
                        + "\"body_fat\": " + "\"" + body_fat + "\", "
                        + "\"diet_goal\": " + "\"" + DGoal + "\", "
                        + "\"body_type\": " + "\"" + bodyT + "\", "
                        + "\"exercise_level\": " + "\"" + exerciseL + "\", "
                        + "\"allergens\": " + "[], "
                        + "\"is_vegetarian\": " + "\"" + checkVeg + "\", "
                        + "\"b_day\": " + "\"" + BDay + "\", "
                        + "\"b_month\": " + "\"" + BMonth + "\", "
                        + "\"b_year\": " + "\"" + BYear + "\"}";

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                putUsertoDB(baseUrl + path, payload);
                Intent intent = new Intent(WalkThroughActivity.this, MainActivity.class);
                intent.putExtra("UID", userID);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });

        btn_Cal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBir();
            }
        });

    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    Gender = "Male";
                break;
            case R.id.radioFemale:
                if (checked)
                    Gender = "Female";
                break;
            case R.id.isVeg:
                if (checked)
                    checkVeg = "True";
                break;
            case R.id.notVeg:
                if (checked)
                    checkVeg = "False";
                break;
        }
    }

    private void chooseBir() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_date, null);
        final DatePicker dateGet = (DatePicker) view.findViewById(R.id.st);
        dateGet.updateDate(dateGet.getYear(), dateGet.getMonth(), 01);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = dateGet.getMonth() + 1;
                String st = "" + dateGet.getYear() + "." + month + "." + dateGet.getDayOfMonth();
                BYear = dateGet.getYear();
                BMonth = month;
                BDay = dateGet.getDayOfMonth();
                textGet.setText(st);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void putUsertoDB(String uri, String payload) {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            //Create connection
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(payload.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            System.out.println(payload);
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(payload);
            wr.close();

            //Get Response

            is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));


            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}