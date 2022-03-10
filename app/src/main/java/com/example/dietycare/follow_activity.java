package com.example.dietycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietycare.Adapter.FollowerAdapter;
import com.example.dietycare.Adapter.FollowingAdapter;
import com.example.dietycare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

//TODO: setup SharedPreferences so both following and follower  can be pressed
public class follow_activity extends AppCompatActivity {

    private ImageButton back_btn;
    private TextView username, following, follow;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<String> following_list = new ArrayList<String>();
    private ArrayList<String> follower_list = new ArrayList<String>();
    private RecyclerView following_view, follower_view;
    private RecyclerView.Adapter followerAdapter, followingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String UID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // binding views
        back_btn = findViewById(R.id.follow_return);
        username = findViewById(R.id.follow_userName);
        following = findViewById(R.id.follow_following);
        follow = findViewById(R.id.follow_follower);
        follower_view = findViewById(R.id.follower_recyclerView);
        follower_view.setVisibility(View.GONE);
        following_view = findViewById(R.id.following_recyclerView);
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //following_list. addAll(Arrays.asList(new String[]{"1", "2", "3", "4"}));



        field_setup();
        following_adapter_setup();
        follower_adapter_setup();

    }

    private void follower_adapter_setup() {

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("follower").child(UID);
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                follower_list = new ArrayList<>();
                for(DataSnapshot temp: snapshot.getChildren()) {
                    follower_list.add(temp.getValue().toString());
                }
                follower_view.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(follow_activity.this);
                follower_view.setLayoutManager(layoutManager);

                followerAdapter = new FollowerAdapter(follow_activity.this, follower_list);
                follower_view.setAdapter(followerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void following_adapter_setup() {
        // retrieve data
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").child(UID);
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following_list = new ArrayList<>();
                for(DataSnapshot temp: snapshot.getChildren()) {
                    following_list.add(temp.getValue().toString());
                }

                following_view.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(follow_activity.this);
                following_view.setLayoutManager(layoutManager);

                followingAdapter = new FollowingAdapter(follow_activity.this, following_list);
                following_view.setAdapter(followingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void field_setup() {
        //set back button
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(follow_activity.this, accountActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //set following btn
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follower_view.setVisibility(View.GONE);
                following_view.setVisibility(View.VISIBLE);
                follow.setTextColor(Color.GRAY);
                following.setTextColor(Color.BLACK);
            }
        });

        //set follower btn
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follower_view.setVisibility(View.VISIBLE);
                following_view.setVisibility(View.GONE);
                follow.setTextColor(Color.BLACK);
                following.setTextColor(Color.GRAY);
            }
        });

        //set username
        database.getReference().child("Users").child(UID).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    username.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}
