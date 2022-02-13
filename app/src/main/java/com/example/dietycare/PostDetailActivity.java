package com.example.dietycare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private ImageButton back_bt, comment_bt;
    private String commentText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //Back Button
        back_bt = findViewById(R.id.icBack);
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

                                           final EditText input = new EditText(PostDetailActivity.this);
                                           input.setInputType(InputType.TYPE_CLASS_TEXT);
                                           builder.setView(input);

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
        postContent.setText("Amysdfasdfas sef");
        //set Post content
        TextView postTime = findViewById(R.id.postTime);
        postTime.setText("10:30 AM");

        //set Comments list
        ListView listview = findViewById(R.id.commentList);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Jason: I love youI love youI love youI love youI love you");
        list.add("2: I love yoI love youI love youI love youu");
        list.add("3: I love yoI love youI love youu");
        list.add("4: I love youI love youI love youI love you");
        list.add("5: I love youI love youI love youI love you");
        list.add("6: I love youI love youI love youI love you");
        list.add("7: I love youI love youI love youI love you");
        StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}