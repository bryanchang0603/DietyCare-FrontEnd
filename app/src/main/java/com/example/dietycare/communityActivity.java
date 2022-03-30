package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietycare.Adapter.communityAdapter;
import com.example.dietycare.Model.Post;
import com.example.dietycare.Notifications.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class communityActivity extends AppCompatActivity {

    public String UID;
    public TextView following_txt, suggest_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageButton home_btn, meal_btn, progress_btn, account_btn, post_btn;

        home_btn = findViewById(R.id.menu_btn_home_community);
        meal_btn = findViewById(R.id.menu_btn_meal_community);
        progress_btn = findViewById(R.id.menu_btn_progress_community);
        account_btn = findViewById(R.id.main_btn_account_community);
        post_btn = findViewById(R.id.community_add_button);
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        following_txt = findViewById(R.id.community_folloeing);
        suggest_txt = findViewById(R.id.community_recommend);

        retrieveData();

        following_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retriveFollowingData();
                following_txt.setTextColor(Color.BLACK);
                suggest_txt.setTextColor(Color.GRAY);
            }
        });

        suggest_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveData();
                following_txt.setTextColor(Color.GRAY);
                suggest_txt.setTextColor(Color.BLACK);
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(communityActivity.this, postingActivity.class);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });



        //bottom buttons
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
                                                 Intent intent = new Intent(communityActivity.this, ProgressActivity.class);
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

    public void retrieveData(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("posts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> postsArr = new ArrayList<Post>();
                for(DataSnapshot temp: snapshot.getChildren()) {
                    String bodyText = temp.child("body_text").getValue().toString();
                    String imgUri = temp.child("image_uri").getValue().toString();
                    String postKey = temp.child("postKey").getValue().toString();
                    long timestamp = (long) temp.child("timeStamp").getValue();
                    String userID = temp.child("userID").getValue().toString();
                    String imgPath = temp.child("image_path").getValue().toString();
                    ArrayList<String> user_liked = (ArrayList<String>) temp.child("user_liked").getValue();
                    int likedNum = (int) temp.child("user_liked").getChildrenCount();
                    Post post = new Post(bodyText, imgUri, userID, postKey, imgPath, user_liked, likedNum);
                    post.setTimeStamp(timestamp);
                    postsArr.add(post);
                }
                Collections.reverse(postsArr);
                RecyclerView recyclerView = findViewById(R.id.community_recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(communityActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                communityAdapter adapter = new communityAdapter(postsArr, communityActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void retriveFollowingData(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("posts");
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").child(UID);
        ArrayList<String> followingUser = new ArrayList<>();

        //getting all following user to an array list
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    followingUser.add(i.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> postsArr = new ArrayList<Post>();
                for(DataSnapshot temp: snapshot.getChildren()) {
                    String bodyText = temp.child("body_text").getValue().toString();
                    String imgUri = temp.child("image_uri").getValue().toString();
                    String postKey = temp.child("postKey").getValue().toString();
                    long timestamp = (long) temp.child("timeStamp").getValue();
                    String userID = temp.child("userID").getValue().toString();
                    String imgPath = temp.child("image_path").getValue().toString();
                    ArrayList<String> user_liked = (ArrayList<String>) temp.child("user_liked").getValue();
                    int likedNum = (int) temp.child("user_liked").getChildrenCount();
                    Post post = new Post(bodyText, imgUri, userID, postKey, imgPath, user_liked, likedNum);
                    post.setTimeStamp(timestamp);
                    if (followingUser.contains(post.getUserID())){
                        postsArr.add(post);
                    }
                }
                Collections.reverse(postsArr);
                RecyclerView recyclerView = findViewById(R.id.community_recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(communityActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                communityAdapter adapter = new communityAdapter(postsArr, communityActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
