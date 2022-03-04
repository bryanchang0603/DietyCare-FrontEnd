package com.example.dietycare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;


import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity{

    private EditText username, email, passwd, passwdCfm, OTP;
    private Button verifyEmailBtn, registerBtn;
    private FirebaseAuth mAuth;
    private ImageButton passwdVisibleBtn, passCfmVisibleBtn;
    private DatabaseReference reference;

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
        email = findViewById(R.id.reg_email);
        passwd = findViewById(R.id.reg_password);
        passwdCfm = findViewById(R.id.reg_confirm_password);
        OTP = findViewById(R.id.otp);
        verifyEmailBtn = findViewById(R.id.button_verify);
        registerBtn = findViewById(R.id.button_register);
        passwdVisibleBtn = findViewById(R.id.password_visible_button);
        passCfmVisibleBtn = findViewById(R.id.confim_password_visible_button);

        passwdVisibleBtn.setOnTouchListener(new View.OnTouchListener() {
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

        passCfmVisibleBtn.setOnTouchListener(new View.OnTouchListener() {
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
                String emailString = email.getText().toString().trim();
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
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        assert firebaseUser != null;
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("username", usernameString);
                                        hashMap.put("imageURL", "default");
                                        hashMap.put("status", "offline");
                                        hashMap.put("search", usernameString.toLowerCase());

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    // TODO: once register page finished, jump to the registering personal info page to allow users to enter their personal info
                                                    Intent intent = new Intent(RegisterActivity.this, WalkThroughActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
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
