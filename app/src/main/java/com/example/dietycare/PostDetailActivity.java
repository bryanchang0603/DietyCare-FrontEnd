package com.example.dietycare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private ImageButton back_bt, comment_bt, like_bt;
    private String commentText = "";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference post_ref, post_comment_ref, post_like_ref;
    private ArrayList<String> comment_list = new ArrayList<>();
    private ArrayList<String> like_lists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //setting up ref to the post
        Intent intent = getIntent();
        String post_path = "posts/"+intent.getStringExtra("postID");
        post_ref = database.getReference().child(post_path);
        post_comment_ref = post_ref.getRef().child("attached_comment");
        post_like_ref = post_ref.getRef().child("user_liked");
        System.out.println(post_comment_ref);
        System.out.println(post_like_ref);

        //retrive comment and like of the post
        // here can also be sued to setup the page information
        post_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comment_list = (ArrayList<String>) snapshot.child("attached_comment").getValue();
                like_lists = (ArrayList<String>) snapshot.child("user_liked").getValue();
                if (comment_list== null){
                    comment_list = new ArrayList<>();
                    post_comment_ref.push();
                }
                if (like_lists==null){
                    like_lists = new ArrayList<>();
                    post_like_ref.push();
                }
                System.out.println(like_lists);
                System.out.println(comment_list);
                System.out.println(like_lists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //Back Button
        back_bt = findViewById(R.id.icBack);
        back_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(PostDetailActivity.this, communityActivity.class);
                                           //Starting of the Intent
                                           startActivity(intent);
                                           finish();
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
                                                   DatabaseReference comment_ref = database.getReference("comments").push();
                                                   String comment_key = comment_ref.getKey();
                                                   String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                   Comment comment = new Comment(commentText, uid, comment_key);
                                                   comment_ref.setValue(comment);
                                                   System.out.println(comment_ref);
                                                   comment_list.add(commentText);
                                                   post_comment_ref.setValue(comment_list).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           Toast.makeText(getBaseContext(), "post added", Toast.LENGTH_LONG).show();
                                                       }
                                                   });
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

        //like button
        like_bt = findViewById(R.id.likeButton);
        String UID = FirebaseAuth.getInstance().getUid();
        System.out.println(UID);
        System.out.println(like_lists);
        like_bt.setColorFilter(like_lists.contains(UID)? Color.RED:Color.GRAY);
        like_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = FirebaseAuth.getInstance().getUid();
                if(like_lists.contains(UID)){
                    like_lists.remove(user);
                    post_like_ref.setValue(like_lists).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            like_bt.setColorFilter(Color.GRAY);
                        }
                    });
                }else{
                    like_lists.add(user);
                    post_like_ref.setValue(like_lists).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            like_bt.setColorFilter(Color.RED);
                        }
                    });
                }
            }
        });

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