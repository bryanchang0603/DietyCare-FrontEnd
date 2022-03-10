package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//TODO: setup SharedPreferences so both following and follower  can be pressed

public class accountActivity extends AppCompatActivity {
    private TextView textGet;
    private String UID;
    private DatabaseReference realtimeDB = FirebaseDatabase.getInstance().getReference();
    private TextView postNum, followingNum, followerNum, Following_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        TextView username = findViewById(R.id.account_user_name);

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
        Following_text = findViewById(R.id.following);

        Following_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(accountActivity.this, follow_activity.class);
                startActivity(intent);
            }
        });


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

        postNum = findViewById(R.id.PostNum);
        DatabaseReference postDBRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int own_post = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    String postUID = i.child("userID").getValue().toString();
                    System.out.println(postUID + UID);
                    own_post += (postUID.equals(UID)) ? 1 : 0;
                }
                postNum.setText(String.valueOf(own_post));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        followingNum = findViewById(R.id.followingNum);
        DatabaseReference followingDBRef = FirebaseDatabase.getInstance().getReference().child("following").child(UID);
        followingDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int following_num = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    following_num += 1;
                }
                followingNum.setText(String.valueOf(following_num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        followerNum = findViewById(R.id.followerNum);
        DatabaseReference followerDBRef = FirebaseDatabase.getInstance().getReference().child("follower").child(UID);
        followerDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int follower_num = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    follower_num += 1;
                }
                followerNum.setText(String.valueOf(follower_num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

