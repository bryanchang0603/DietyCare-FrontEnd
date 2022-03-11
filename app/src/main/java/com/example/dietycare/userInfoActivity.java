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

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class userInfoActivity extends AppCompatActivity {
    private ImageButton back_bt;
    private String UID, user_diet_goal = "Lose Weight";
    private int num_of_posts = 1, num_of_followings = 3, num_of_followers = 5, days_on_dietycare = 123;
    private DatabaseReference realtimeDB = FirebaseDatabase.getInstance().getReference();
    private JSONObject current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        // get userId that we are working with
        Intent intent = getIntent();
        UID = intent.getStringExtra("postOwnerId");

        // get this user from DynamoDB
        Thread thread = new Thread() {
            public void run() {
                try {
                    current_user = getUser(UID);
                    user_diet_goal = current_user.getString("diet_goal");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

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

