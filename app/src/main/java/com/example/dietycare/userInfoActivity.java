package com.example.dietycare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userInfoActivity extends AppCompatActivity {
    private ImageButton back_bt;
    private String UID, user_diet_goal = "Lose Weight";
    private int num_of_posts = 1, num_of_followings = 3, num_of_followers = 5, days_on_dietycare = 123;
    private DatabaseReference realtimeDB = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        TextView username = findViewById(R.id.account_user_name);

        // set post, following, follower numbers
        TextView posts = findViewById(R.id.textView17);
        TextView followings = findViewById(R.id.textView19);
        TextView followers = findViewById(R.id.textView21);
        TextView diet_goal = findViewById(R.id.textView22);
        TextView days_on_dc = findViewById(R.id.textView26);
        posts.setText(String.valueOf(num_of_posts));
        followings.setText(String.valueOf(num_of_followings));
        followers.setText(String.valueOf(num_of_followers));
        diet_goal.setText(user_diet_goal);
        days_on_dc.setText(String.valueOf(days_on_dietycare));

        //Back Button
        back_bt = findViewById(R.id.icBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(userInfoActivity.this, communityActivity.class);
                                           //Starting of the Intent
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
        );

        // TODO change to the actual user's uid instead of current user
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // the following listener will read the current user's username and upload to the page
        realtimeDB.child("Users").child(UID).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    username.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

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
                Intent intent = new Intent(userInfoActivity.this, editActivity.class);
                startActivity(intent);
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(userInfoActivity.this, LoginActivity.class);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, messageActivity.class);
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
                                                Intent intent = new Intent(userInfoActivity.this, progress.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        meal_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(userInfoActivity.this, MealRecommendationActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );

        community_btn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(userInfoActivity.this, communityActivity.class);
                                                 //Starting of the Intent
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         }
        );

        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(userInfoActivity.this, MainActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
        );
    }
}

