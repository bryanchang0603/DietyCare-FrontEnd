package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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


public class RegisterActivity extends AppCompatActivity{

    private EditText username, emailAddress, passwd, passwdCfm, OTP;
    private Button verifyEmailBtn, registerBtn;
    private FirebaseAuth mAuth;
    private ImageButton passwdBtn, passCfmBtn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.reg_username);
        emailAddress = findViewById(R.id.reg_email);
        passwd = findViewById(R.id.reg_password);
        passwdCfm = findViewById(R.id.reg_confirm_password);
        OTP = findViewById(R.id.otp);
        verifyEmailBtn = findViewById(R.id.button_verify);
        registerBtn = findViewById(R.id.button_register);
        passwdBtn = findViewById(R.id.password_visible_button);
        passCfmBtn = findViewById(R.id.confim_password_visible_button);

        passwdBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    passwd.setInputType(InputType.TYPE_CLASS_TEXT);
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    return true;
                }
                return false;
            }
        });

        passCfmBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    passwdCfm.setInputType(InputType.TYPE_CLASS_TEXT);
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    passwdCfm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    return true;
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String emailString = emailAddress.getText().toString().trim();
                String usernameString = username.getText().toString().trim();
                String pwdString = passwd.getText().toString().trim();
                String pwdCfmString = passwdCfm.getText().toString().trim();

                if (!emailVerify(emailString)){
                    Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(usernameString)
                            && !TextUtils.isEmpty(pwdString) && !TextUtils.isEmpty(pwdCfmString)){
                        if(pwdString.equals(pwdCfmString)){
                            mAuth.createUserWithEmailAndPassword(emailString, pwdString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            );
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(TextUtils.isEmpty(emailString)){
                        Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(usernameString)){
                        Toast.makeText(RegisterActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(pwdString)){
                        Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(pwdCfmString)){
                        Toast.makeText(RegisterActivity.this, "Please enter confirmed password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean emailVerify(String email){
        int flag=0,f=0;
        for(int i=0;i<email.length();i++){
            if(email.charAt(i) == '@'){
                flag=1;
            } else if(email.charAt(i) == '.' && flag==1){
                flag=2;
            } else if((flag==2 && (int)email.charAt(i) >= 97 && (int)email.charAt(i) <= 122)){
                f=1;
            } else if(f==1){
                return false;
            }
        }
        if(f==1){
            return true;
        } else{
            return false;
        }
    }

}
