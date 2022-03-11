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

        Intent intent = getIntent();
        UID = intent.getStringExtra("postOwnerId");

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

    }
}

