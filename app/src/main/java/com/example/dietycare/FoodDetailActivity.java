package com.example.dietycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    private Button record_bt, more_bt;
    private ImageButton back_bt;
    private TextView food_name_tv,food_info_tv;
    private TextView cal_val_tv,pro_val_tv,fat_val_tv,carbo_val_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        Intent intent = getIntent();
        String food_name = intent.getStringExtra("food name");
        set_food_name_tv(food_name);
        Double calorie = intent.getDoubleExtra("calorie",0.0);
        String cal_val = calorie.toString()+"kcal";
        set_cal_val_tv(cal_val);
        Double protein = intent.getDoubleExtra("protein",0.0);
        String pro_val = protein.toString()+"g";
        set_pro_val_tv(pro_val);
        Double fat = intent.getDoubleExtra("fat",0.0);
        String fat_val = fat.toString()+"g";
        set_fat_val_tv(fat_val);
        Double carbo= intent.getDoubleExtra("carbo",0.0);
        String carbo_val = carbo.toString()+"g";
        set_carbo_val_tv(carbo_val);
        String food_info = food_name+"\n"+cal_val;
        set_food_info_tv(food_info);

        //Back Button
        back_bt = (ImageButton) findViewById(R.id.icBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(FoodDetailActivity.this, searchResultActivity.class);
                                           //Starting of the Intent
                                           startActivity(intent);

                                       }
                                   }
        );

        /*//More Button
        more_bt = (Button) findViewById(R.id.moreButton);
        more_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(FoodDetailActivity.this, MoreNutrientActivity.class);
                                           //Starting of the Intent
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
        );*/

        //Record Button
        record_bt = (Button) findViewById(R.id.recordButton);
        record_bt.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(FoodDetailActivity.this, RecordActivity.class);
                                             //Starting of the Intent
                                             intent.putExtra("food_name", food_name);
                                             intent.putExtra("protein", protein);
                                             intent.putExtra("fat", fat);
                                             intent.putExtra("carbo", carbo);
                                             intent.putExtra("calorie", calorie);
                                             startActivity(intent);
                                             finish();
                                         }
                                     }
        );

    }

    //Set the values of TextViews
    private void set_food_name_tv(String food_name){
        food_name_tv = (TextView) findViewById(R.id.foodName);
        food_name_tv.setText(food_name);
    }

    private void set_food_info_tv(String food_info){
        food_info_tv = (TextView) findViewById(R.id.foodInfo);
        food_info_tv.setText(food_info);
    }

    private void set_cal_val_tv (String cal_val){
        cal_val_tv = (TextView) findViewById(R.id.calorieVal);
        cal_val_tv.setText(cal_val);
    }

    private void set_pro_val_tv (String pro_val){
        pro_val_tv = (TextView) findViewById(R.id.proteinVal);
        pro_val_tv.setText(pro_val);
    }

    private void set_fat_val_tv (String fat_val){
        fat_val_tv = (TextView) findViewById(R.id.fatVal);
        fat_val_tv.setText(fat_val);
    }

    private void set_carbo_val_tv (String carbo_val){
        carbo_val_tv = (TextView) findViewById(R.id.carboVal);
        carbo_val_tv.setText(carbo_val);
    }

}