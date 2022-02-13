package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class communityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageButton home_btn, meal_btn, progress_btn, account_btn;

        home_btn = findViewById(R.id.menu_btn_home_community);
        meal_btn = findViewById(R.id.menu_btn_meal_community);
        progress_btn = findViewById(R.id.menu_btn_progress_community);
        account_btn = findViewById(R.id.main_btn_account_community);

        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(communityActivity.this, MainActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(communityActivity.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        progress_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(communityActivity.this, progress.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        account_btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(communityActivity.this, accountActivity.class);
                                               //Starting of the Intent
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );
    }

}
