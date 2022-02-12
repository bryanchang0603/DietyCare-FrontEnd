package com.example.dietycare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    private Button record_bt, more_bt;
    private ImageButton back_bt, comment_bt;
    private String commentText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //Back Button
        back_bt = (ImageButton) findViewById(R.id.icBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
                                           //Starting of the Intent
                                           startActivity(intent);

                                       }
                                   }
        );

        //comment Button
        comment_bt = findViewById(R.id.commentButton);
        comment_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
                                           builder.setTitle("Comment This Post");

// Set up the input
                                           final EditText input = new EditText(PostDetailActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                           input.setInputType(InputType.TYPE_CLASS_TEXT);
                                           builder.setView(input);

// Set up the buttons
                                           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   commentText = input.getText().toString();
                                               }
                                           });
                                           builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   dialog.cancel();
                                               }
                                           });

                                           builder.show();

                                       }
                                   }
        );

        //set Post Owner's Name
        TextView postOwnerName = findViewById(R.id.postOwnerName);
        postOwnerName.setText("Amy");
        //set Post content
        TextView postContent = findViewById(R.id.postContent);
        postContent.setText("Amysdfasdfas sef asAmysdfasdfas sef asfsdafsdoiafjsj dsaf sda fsdfadsfawgwegdfgdafg");
        //set Post content
        TextView postTime = findViewById(R.id.postTime);
        postTime.setText("10:30 AM");

    }


    private String get_food_name() {
        return "Apple";
    }
    private String get_food_info (){
        return "Apple is 11.5 kcal.";
    };
    private String get_cal (){
        return "11.5kcal";
    };//Double to String;
    private String get_pro (){
        return "50g";
    };
    private String get_fat (){
        return "40g";
    };
    private String get_carbo (){
        return "60g";
    };
}