package com.example.dietycare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecordActivity extends AppCompatActivity {

    private Button cancel_bt, save_bt;
    private Integer cur_year, cur_month, cur_day;
    private Double cal_intake, pro_intake, fat_intake, carbo_intake;
    private String meal_ty, result;
    private ImageButton date_bt;
    private TextView food_tv, date_tv, cal_tv, amount_tv;
    private EditText amount_et;
    private Spinner meal_type_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Intent intent = getIntent();
        String food_name = intent.getStringExtra("food_name");
        Double food_cal = intent.getDoubleExtra("calorie",0.0);
        Double food_pro = intent.getDoubleExtra("protein",0.0);
        Double food_fat = intent.getDoubleExtra("fat",0.0);
        Double food_carbo= intent.getDoubleExtra("carbo",0.0);

        //Cancel Button
        cancel_bt = (Button) findViewById(R.id.cancel);
        cancel_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(RecordActivity.this, FoodDetailActivity.class);
                                           //Starting of the Intent
                                           intent.putExtra("food name", food_name);
                                           intent.putExtra("protein", food_pro);
                                           intent.putExtra("fat", food_fat);
                                           intent.putExtra("carbo", food_fat);
                                           intent.putExtra("calorie", food_cal);
                                           startActivity(intent);

                                       }
                                   }
        );

        //Food name from food_detail page
        food_tv = findViewById(R.id.food);
        food_tv.setText(food_name);

        //Date selection
        date_tv = findViewById(R.id.selectedDate);
        date_bt =(ImageButton) findViewById(R.id.imageCal);
        date_bt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        //Spinner
        String[] meal_type = {"Breakfast","Brunch","Lunch","Dinner"};
        meal_type_sp = findViewById(R.id.selectedType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, meal_type);
        meal_type_sp.setAdapter(adapter);
        meal_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meal_ty = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cal_tv = (TextView) findViewById(R.id.kal);
        cal_tv.setText("0");
        amount_tv=(TextView) findViewById(R.id.g);
        amount_tv.setText("0");

        amount_et = (EditText) findViewById(R.id.amount);
        amount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if(amount_et.getText().length() >= 0) {
                    result = amount_et.getText().toString();
                    cal_intake = food_cal * Double.parseDouble(result) / 100.0;
                    pro_intake = food_pro * Double.parseDouble(result) / 100.0;
                    fat_intake = food_fat * Double.parseDouble(result) / 100.0;
                    carbo_intake = food_carbo * Double.parseDouble(result) / 100.0;
                    cal_tv.setText(cal_intake.toString());
                    amount_tv.setText(result);
                }
            }
        });

        //Save record
        String user_id = "123";
        String date = cur_year+"-"+cur_month+"-"+cur_day;
        save_bt = (Button) findViewById(R.id.save);
        save_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Thread thread = new Thread() {
                                               public void run() {
                                                   try {
                                                       saveData(user_id, food_name, result, date, meal_ty,
                                                               cal_intake, pro_intake, fat_intake, carbo_intake);
                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                   }
                                               }
                                           };
                                           thread.start();
                                           Intent intent = new Intent(RecordActivity.this, FoodDetailActivity.class);
                                           //Starting of the Intent
                                           intent.putExtra("food name", food_name);
                                           intent.putExtra("protein", food_pro);
                                           intent.putExtra("fat", food_fat);
                                           intent.putExtra("carbo", food_fat);
                                           intent.putExtra("calorie", food_cal);
                                           startActivity(intent);
                                       }

                                   }
        );


    }
    private void selectDate() {
        View view = LayoutInflater.from(this).inflate(R.layout.date_selection, null);
        final DatePicker dateGet = (DatePicker) view.findViewById(R.id.st);
        dateGet.updateDate(dateGet.getYear(), dateGet.getMonth(), 01);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = dateGet.getMonth() + 1;
                String st = "" + dateGet.getYear() + "." + month + "." + dateGet.getDayOfMonth();
                cur_year = dateGet.getYear();
                cur_month = month;
                cur_day = dateGet.getDayOfMonth();
                date_tv.setText(st);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void saveData(String user_id, String food_name, String amount, String date,
                          String meal_ty, Double cal_intake, Double pro_intake,
                          Double fat_intake, Double carbo_intake) throws Exception {
        String baseUrl = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com";
        String path = "/dishIntake?";
        /*String params = "user_id="+user_id+"&" + "food_name="+food_name+"&"
                + "food_intake_amount="+amount+"&" + "intake_date="+date
                +"&"+ "meal_type"+meal_ty+"&"+ "cal_consumed="
                +cal_intake+"&" + "pro_consumed="+pro_intake+"&"
                + "fat_consumed="+fat_intake+"&" + "CHO_consumed="+carbo_intake;*/
        String params = "user_id=123&" + "food_name="+food_name+"&"
                + "food_intake_amount="+amount+"&" + "intake_date=2022-01-14&"
                + "meal_type=Breakfast&"+ "cal_consumed="
                +cal_intake+"&" + "pro_consumed="+pro_intake+"&"
                + "fat_consumed="+fat_intake+"&" + "CHO_consumed="+carbo_intake;
        put(baseUrl + path + params);


    }


    public static void put(String uri) throws Exception {
        URL url = new URL(uri) ;
        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestMethod("PUT");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(responseStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String response = buf.toString("UTF-8");;

    }
}