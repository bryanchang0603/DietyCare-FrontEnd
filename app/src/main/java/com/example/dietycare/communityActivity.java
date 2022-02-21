package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class communityActivity extends AppCompatActivity {
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

        retrieveData();



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

                    Post post = new Post(bodyText, imgUri, userID, postKey, imgPath, user_liked);
                    post.setTimeStamp(timestamp);
                    postsArr.add(post);

                    RecyclerView recyclerView = findViewById(R.id.community_recyclerView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(communityActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    communityAdapter adapter = new communityAdapter(postsArr, communityActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
