package com.example.dietycare;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private ImageButton back_bt;
    private String UID, user_diet_goal = "Lose Weight";
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
        UID = intent.getStringExtra("postOwnerId");

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
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(userInfoActivity.this).load(uri).fitCenter().into((ImageView) findViewById(R.id.postPicture1));
                            }
                        });
                    }
                    else if (counter == 2){
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(userInfoActivity.this).load(uri).fitCenter().into((ImageView) findViewById(R.id.postPicture2));
                            }
                        });
                    }
                    else if (counter == 1){
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

