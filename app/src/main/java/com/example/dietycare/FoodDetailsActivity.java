package com.example.dietycare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class FoodDetailsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        Intent intent = getIntent();
        String foodName = intent.getStringExtra("food name");
        Double protein = intent.getDoubleExtra("protein",0.0);
        Double fat = intent.getDoubleExtra("fat",0.0);
        Double carbo = intent.getDoubleExtra("carbo",0.0);
        Double calorie = intent.getDoubleExtra("calorie",0.0);
        TextView foodDetailProtein = findViewById(R.id.food_detail_protein);
        foodDetailProtein.setText(protein.toString());
    }
}
