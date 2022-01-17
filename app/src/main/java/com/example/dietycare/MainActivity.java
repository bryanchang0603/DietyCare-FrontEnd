package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private ImageButton home_btn, progress_btn, meal_btn, community_btn, account_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        home_btn = findViewById(R.id.menu_btn_home);
        progress_btn = findViewById(R.id.menu_btn_progress);
        meal_btn = findViewById(R.id.menu_btn_meal);
        community_btn = findViewById(R.id.menu_btn_community);
        account_btn = findViewById(R.id.main_btn_account);


        home_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            setContentView(R.layout.main);
                                        }
                                    }
        );



/*        progress_btn.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                setContentView(R.layout.progress);
                                            }
                                        }
        );*/

        progress_btn.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(MainActivity.this, accountActivity.class);
                                                //Starting of the Intent
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
        );

        meal_btn.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                setContentView(R.layout.activity_meal_plan);
                                            }
                                        }
        );

        community_btn.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            setContentView(R.layout.share);
                                        }
                                    }
        );

        account_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                                                 setContentView(R.layout.account);
                                             }
                                         }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseAuth.getInstance().signOut(); // this is for login test purpose need to remove in integration
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "user not login", Toast.LENGTH_SHORT).show();
            sendToLogin();
        }

    }


    private void sendToLogin() {

        //Declaration of explict Intent from MainActivity to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        //Starting of the Intent
        startActivity(intent);
        finish();
    }
}