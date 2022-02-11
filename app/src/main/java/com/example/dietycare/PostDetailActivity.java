package com.example.dietycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    private Button record_bt, more_bt;
    private ImageButton back_bt;
    private TextView food_name_tv,food_info_tv;
    private TextView cal_val_tv,pro_val_tv,fat_val_tv,carbo_val_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //Back Button
        back_bt = (ImageButton) findViewById(R.id.icBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
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

    }


    private String get_food_name() {
        return "Apple";
    }
    private String get_food_info (){
        return "Apple is 11.5 kcal.";
    };
    private String get_cal (){
        return "11.5kcal";
    };//Double to String;
    private String get_pro (){
        return "50g";
    };
    private String get_fat (){
        return "40g";
    };
    private String get_carbo (){
        return "60g";
    };
}