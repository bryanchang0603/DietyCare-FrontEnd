package com.example.dietycare;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class userInfoActivity extends AppCompatActivity {
    private ImageButton back_bt, add_button, chat_button, remove_button;
    private ImageButton btn_progress, btn_meal, btn_home, btn_community, btn_account;
    private String UID, user_diet_goal = "Lose Weight", current_UID;
    private TextView username, posts, followings, followers, diet_goal ,days_on_dc;
    private ImageView post_pic_1, post_pic_2, post_pic_3;
    private int num_of_posts = 1, num_of_followings = 3, num_of_followers = 5;
    private long days_on_dietycare = 123;
    private DatabaseReference realtimeDB = FirebaseDatabase.getInstance().getReference();
    private JSONObject current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        // get userId that we are working with
        Intent intent = getIntent();
        UID = intent.getStringExtra("postOwnerId");// the specific user's UID
        current_UID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // current login user's UID

        // get this user from DynamoDB
        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                try {
                    current_user = getUser(UID);
                    user_diet_goal = current_user.getString("diet_goal");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
                    LocalDate registration_date = LocalDate.parse(current_user.getString("registration_date"), dtf);
                    LocalDate now = LocalDate.parse(LocalDate.now().format(dtf), dtf);
                    days_on_dietycare = Duration.between(registration_date.atStartOfDay(), now.atStartOfDay()).toDays();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        username = findViewById(R.id.account_user_name);

        // set post, following, follower numbers
        posts = findViewById(R.id.textView17);
        followings = findViewById(R.id.textView19);
        followers = findViewById(R.id.textView21);
        diet_goal = findViewById(R.id.textView22);
        days_on_dc = findViewById(R.id.textView26);
        add_button = findViewById(R.id.community_add_button);
        chat_button = findViewById(R.id.community_chat_button);
        remove_button = findViewById(R.id.community_remove_button);
        btn_progress = findViewById(R.id.btn_progress);
        btn_meal = findViewById(R.id.btn_meal);
        btn_home = findViewById(R.id.btn_home);
        btn_community = findViewById(R.id.btn_community);
        btn_account = findViewById(R.id.btn_account);
        post_pic_1 = findViewById(R.id.postPicture1);
        post_pic_2 = findViewById(R.id.postPicture2);
        post_pic_3 = findViewById(R.id.postPicture3);
        posts.setText(String.valueOf(num_of_posts));
        followings.setText(String.valueOf(num_of_followings));
        followers.setText(String.valueOf(num_of_followers));
        diet_goal.setText(user_diet_goal);
        days_on_dc.setText(String.valueOf(days_on_dietycare));
        post_pic_1.setVisibility(View.GONE);
        post_pic_2.setVisibility(View.GONE);
        post_pic_3.setVisibility(View.GONE);
        remove_button.setVisibility(View.GONE);


        page_setup();
        user_post_image_setup();
        firebase_check();
        menu_button_setup();
    }

    private void menu_button_setup() {
        btn_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, ProgressActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, MealRecommendationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, communityActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userInfoActivity.this, accountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void page_setup(){
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

        // disable chat and follow button if opens ones own personal page
        if (current_UID.equals(UID)){
            add_button.setVisibility(View.GONE);
            chat_button.setVisibility(View.GONE);
        }

        // check if the current user followed this user
        DatabaseReference followerDBRef = realtimeDB.child("follower").child(UID);
        followerDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean is_followed = false;
                for (DataSnapshot i : snapshot.getChildren()){
                    if(i.getValue().toString().equals(current_UID)){
                        add_button.setVisibility(View.GONE);
                        remove_button.setVisibility(View.VISIBLE);
                        is_followed = true;
                    }
                }

                if (!is_followed){
                    add_button.setVisibility(View.VISIBLE);
                    remove_button.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setup remove_button
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realtimeDB.child("following").child(current_UID).child(UID).removeValue();
                realtimeDB.child("follower").child(UID).child(current_UID).removeValue();
            }
        });

        //setup add_button
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding the specific user to the current user's following table
                DatabaseReference following_ref = realtimeDB.child("following").child(current_UID).child(UID);
                following_ref.setValue(UID);

                //adding the current user to the specific user's follower table
                DatabaseReference follower_ref = realtimeDB.child("follower").child(UID).child(current_UID);
                follower_ref.setValue(current_UID);
            }
        });


        //setup chat_button
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (userInfoActivity.this, MessagePageActivity.class);
                intent.putExtra("userid", UID);
                startActivity(intent);
                finish();
            }
        });
    }

    private void user_post_image_setup(){

        realtimeDB.child("posts").orderByChild("userID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 3;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if (counter == 0) break;
                    String image_path = (String) postSnapshot.child("image_path").getValue();
                    System.out.println(image_path);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(image_path);
                    if (counter == 3){
                        post_pic_1.setVisibility(View.VISIBLE);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(userInfoActivity.this).load(uri).fitCenter().into((ImageView) findViewById(R.id.postPicture1));
                            }
                        });
                    }
                    else if (counter == 2){
                        post_pic_2.setVisibility(View.VISIBLE);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(userInfoActivity.this).load(uri).fitCenter().into((ImageView) findViewById(R.id.postPicture2));
                            }
                        });
                    }
                    else if (counter == 1){
                        post_pic_3.setVisibility(View.VISIBLE);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(userInfoActivity.this).load(uri).fitCenter().into((ImageView) findViewById(R.id.postPicture3));
                            }
                        });
                    }
                    counter--;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firebase_check(){

        //Get the number of post of the specific user
        DatabaseReference postDBRef = realtimeDB.child("posts");
        postDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int own_post = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    String postUID = i.child("userID").getValue().toString();
                    own_post += (postUID.equals(UID)) ? 1 : 0;
                }
                posts.setText(String.valueOf(own_post));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set the following number and follower number of the specific user
        DatabaseReference followingDBRef = realtimeDB.child("following").child(UID);
        followingDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int following_num = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    following_num += 1;
                }
                followings.setText(String.valueOf(following_num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference followerDBRef = realtimeDB.child("follower").child(UID);
        followerDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int follower_num = 0;
                for (DataSnapshot i : snapshot.getChildren()){
                    follower_num += 1;
                }
                followers.setText(String.valueOf(follower_num));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private JSONObject getUser(String userId) throws Exception{
        // Create a neat value object to hold the URL
        String str_url = "http://flask-env.eba-vyrxyu72.us-east-1.elasticbeanstalk.com/user?user_id=" + userId;
        URL url = new URL(str_url) ;
        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(responseStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String response = buf.toString("UTF-8");

        final JSONObject obj = new JSONObject(response);

        return obj;


    }
}

