package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView registerButton;
    private FirebaseAuth mAuth;
    private ImageButton signiPasswordVisible;
    @Override
    protected void onStart() {

        //super is called to restore the previous super class onStart()data
        super.onStart();
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        FirebaseAuth.getInstance().signOut();
        //FirebaseUser class is public abstract class FirebaseUser extends Object
        //implements Parcelable UserInfo which is used in helping to check if the user is logged in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //If user is logged in then send him to the MianActivity
            sendToMain();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        //get the instance for firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // open the signin.xml


        loginEmail = findViewById(R.id.sign_in_username);
        loginPassword = findViewById(R.id.sign_in_password);
        loginButton = findViewById(R.id.button_signin);
        registerButton = findViewById(R.id.signin_register);
        signiPasswordVisible = findViewById(R.id.sigin_show_passwd);

        signiPasswordVisible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String loginEmailStr = loginEmail.getText().toString().trim();
                String loginPasswordlStr = loginPassword.getText().toString().trim();

                // check if the login or password are empty
                if (!(TextUtils.isEmpty(loginEmailStr) || TextUtils.isEmpty(loginPasswordlStr))){
                    mAuth.signInWithEmailAndPassword(loginEmailStr, loginPasswordlStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendToMain();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Email or Password can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

    }

    private void sendToMain()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}