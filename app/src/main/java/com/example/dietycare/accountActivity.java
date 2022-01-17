package com.example.dietycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class accountActivity extends AppCompatActivity{
    private TextView textGet, signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Button btn_register = findViewById(R.id.material1);
        signOut = findViewById(R.id.signout_field);

        androidx.appcompat.widget.AppCompatImageView homeBtn = findViewById(R.id.home);

        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(accountActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(accountActivity.this, LoginActivity.class);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });


/*        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(accountActivity.this,editActivity.class);
                startActivity(intent);
            }
        });*/
    }
}