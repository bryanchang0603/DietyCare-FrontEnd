package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class accountActivity extends AppCompatActivity {
    private TextView textGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Button btn_edit = findViewById(R.id.material1);
        ImageButton home_btn, progress_btn, meal_btn, community_btn;
        TextView sign_out = findViewById(R.id.signout_field);
        TextView message_btn = findViewById(R.id.textView15);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(accountActivity.this, editActivity.class);
                startActivity(intent);
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(accountActivity.this, LoginActivity.class);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(accountActivity.this, messageActivity.class);
                //Starting of the Intent
                startActivity(intent);
            }
        });

        home_btn = findViewById(R.id.menu_btn_home_account);
        progress_btn = findViewById(R.id.menu_btn_progress_account);
        meal_btn = findViewById(R.id.menu_btn_meal_account);
        community_btn = findViewById(R.id.menu_btn_community_account);


        progress_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(accountActivity.this, progress.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(accountActivity.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(accountActivity.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(accountActivity.this, MainActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );
    }
}

