package com.example.dietycare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {

    private ImageButton back_bt, comment_bt, like_bt;
    private String commentText = "";
    private String post_path;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference post_ref, post_like_ref;
    private ArrayList<String> like_lists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //setting up ref to the post
        Intent intent = getIntent();
        post_path = "posts/"+intent.getStringExtra("postID");
        post_ref = database.getReference().child(post_path);
        post_like_ref = post_ref.getRef().child("user_liked");
        System.out.println(post_like_ref);

        //like button initalization for color setup
        like_bt = findViewById(R.id.likeButton);
        String UID = FirebaseAuth.getInstance().getUid();

        //retrive comment and like of the post
        // here can also be used to setup the page information
        post_ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                like_lists = (ArrayList<String>) snapshot.child("user_liked").getValue();
                if (like_lists==null){
                    like_lists = new ArrayList<>();
                    post_like_ref.push();
                }
                like_bt.setColorFilter(like_lists.contains(UID)? Color.RED:Color.GRAY);
/*                System.out.println(like_lists);
                System.out.println(comment_list);
                System.out.println(like_lists);*/

                //set Post Owner's Name
                TextView postOwnerName = findViewById(R.id.postOwnerName);
                postOwnerName.setText(snapshot.child("userID").getValue().toString());
                //set Post content
                TextView postContent = findViewById(R.id.postContent);
                postContent.setText(snapshot.child("body_text").getValue().toString());
                //set Post time
                TextView postTime = findViewById(R.id.postTime);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(snapshot.child("timeStamp").getValue().toString()));
                postTime.setText(sf.format(date));
                //set Comments list
                ListView listview = findViewById(R.id.commentList);
                HashMap comments;
                ArrayList<String> commentList = new ArrayList<>();
                if((comments = (HashMap<String, HashMap>) snapshot.child("attached_comment").getValue()) != null){
                    for (HashMap comment : (Collection<HashMap>) comments.values()) {
                        String commentUserID = (String) comment.get("userID");
                        String commentText = (String) comment.get("comment_text");
                        String timeStamp = sf.format(new Date(Long.parseLong(comment.get("timeStamp").toString())));
                        commentList.add(String.format("%s: %s  %s", commentUserID, commentText, timeStamp));
                    }
                    commentList.sort(Comparator
                            .comparing(s -> ((String) s).substring(s.length() - 19), Comparator.reverseOrder()));
                }
                StableArrayAdapter adapter = new StableArrayAdapter(PostDetailActivity.this,
                        android.R.layout.simple_list_item_1, commentList);
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
                                                   DatabaseReference comment_ref = database.getReference(post_path).child("attached_comment").push();
                                                   String comment_key = comment_ref.getKey();
                                                   String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                   Comment comment = new Comment(commentText, uid, comment_key);
                                                   comment_ref.setValue(comment);
/*                                                   System.out.println(comment_ref);*/
                     /*                              comment_list.add(comment_key);
                                                   post_comment_ref.setValue(comment_list).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           Toast.makeText(getBaseContext(), "post added", Toast.LENGTH_LONG).show();
                                                       }
                                                   });*/
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