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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class editActivity extends AppCompatActivity{
    private HashMap<String, String> readInfo = new HashMap<String, String>();
    private static final int SELECT_PICTURE = 1;
    private TextView textGet;
    private String bodyT, exerciseL;
    private int BYear, BMonth, BDay;
    private String Gender = "Male";
    private String checkVeg = "False";

    String[] bodyType = {"Ectomorph", "Endomorph", "Mesomorph"};
    String[] exercise_level = {"1", "2", "3", "4", "5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap<String, String> readInfo = new HashMap<String, String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Spinner sp = (Spinner) findViewById(R.id.spinner3);
        Spinner exer_sp = (Spinner) findViewById(R.id.spinner_exercise);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bodyType);
        sp.setAdapter(adapter);
        ArrayAdapter<String> exer_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, exercise_level);
        exer_sp.setAdapter(exer_adapter);
        EditText weight = findViewById(R.id.editTextNumber5);
        EditText BFat= findViewById(R.id.editTextNumber6);
        EditText Tweight = findViewById(R.id.editTextNumber7);
        EditText height = findViewById(R.id.editTextNumber8);
        textGet = findViewById(R.id.textView);
        RadioButton Yes = findViewById(R.id.isVeg);
        RadioButton No = findViewById(R.id.notVeg);
        RadioButton maleB = findViewById(R.id.radioMale);
        RadioButton femaleB = findViewById(R.id.radioFemale);
        ImageButton btn_register = findViewById(R.id.imageButton2);
        ImageButton btn_Cal = findViewById(R.id.btncal);
        ImageButton btn_gallery = findViewById(R.id.imageButton3);
        Button btn_save = findViewById(R.id.saveBtn);

        //Get user id from register
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String userID = firebaseUser.getUid();

        //read data from DB
        String baseUrlforGet = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com";
        String pathforGet = "/user?";
        String params = "user_id=" + userID;
        String a = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            a = getUserfromDB(baseUrlforGet + pathforGet + params);
        } catch (Exception e) {

        }
        a = a.replaceAll("\\s+","");
        a = a.replaceAll("\\{","");
        a = a.replaceAll("\\}","");
        String[] pairs = a.split(",");
        for(int i = 0; i < pairs.length; i++) {
            String temp = pairs[i].replaceAll("\"", "");
            String[] hashElement = temp.split(":");
            readInfo.put(hashElement[0], hashElement[1]);
        }

        //write data from DB
        weight.setText(readInfo.get("weight"));
        float temp = Float.parseFloat(readInfo.get("body_fat"));
        temp = temp * 100;
        String bodyFatReal = String.format("%.1f", temp);
        BFat.setText(bodyFatReal);
        Tweight.setText(readInfo.get("target_weight"));
        height.setText(readInfo.get("height"));

        BYear = Integer.valueOf(readInfo.get("b_year"));
        BMonth = Integer.valueOf(readInfo.get("b_month"));
        BDay = Integer.valueOf(readInfo.get("b_day"));

        textGet.setText(BYear + "." + BMonth + "." + BDay);

        int index = 0;

        if(readInfo.get("body_type").equals("Endomorph")){
            index = 1;
        }
        if(readInfo.get("body_type").equals("Mesomorph")){
            index = 2;
        }
        sp.setSelection(index);

        int index_exer = 0;

        if(readInfo.get("exercise_level").equals("2")){
            index_exer = 1;
        }
        if(readInfo.get("exercise_level").equals("3")){
            index_exer = 2;
        }
        if(readInfo.get("exercise_level").equals("4")){
            index_exer = 3;
        }
        if(readInfo.get("exercise_level").equals("5")){
            index_exer = 4;
        }
        exer_sp.setSelection(index_exer);

        if(readInfo.get("is_vegetarian").equals("true")) {
            Yes.setChecked(true);
            No.setChecked(false);
        }else{
            No.setChecked(true);
            Yes.setChecked(false);
        }

        if(readInfo.get("gender").equals("Male")) {
            maleB.setChecked(true);
            femaleB.setChecked(false);
        }else{
            femaleB.setChecked(true);
            maleB.setChecked(false);
        }

        //
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

        btn_save.setOnClickListener( new View.OnClickListener(){
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
                Toast.makeText(getApplicationContext(), "successfully saved", Toast.LENGTH_SHORT).show();
            }
        });

        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Cal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBir();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
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

    public static String getUserfromDB(String uri) throws Exception {
        URL url = new URL(uri) ;
        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestMethod("GET");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(responseStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String response = buf.toString("UTF-8");

        return response;

    }

}