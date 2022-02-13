package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class progress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        ImageButton home_btn, meal_btn, community_btn, account_btn;

        home_btn = findViewById(R.id.menu_btn_home_progress);
        meal_btn = findViewById(R.id.menu_btn_meal_progress);
        community_btn = findViewById(R.id.menu_btn_community_progress);
        account_btn = findViewById(R.id.main_btn_account_progress);

        home_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(progress.this, MainActivity.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(progress.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(progress.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        account_btn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(progress.this, accountActivity.class);
                                               //Starting of the Intent
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );
    }

}
