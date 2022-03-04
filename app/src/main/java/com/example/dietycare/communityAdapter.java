package com.example.dietycare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class communityAdapter extends RecyclerView.Adapter<communityAdapter.Holder> {

    private ArrayList<Post> postsArr;
    private Context context;
    private DatabaseReference realtimeDB = FirebaseDatabase.getInstance().getReference();
    ArrayList<ArrayList<Post>> twoPostsAsAGroup;

    public communityAdapter(ArrayList<Post> postsArr, Context context) {
        twoPostsAsAGroup = new ArrayList<ArrayList<Post>>();
        for (int i = 0; i < postsArr.size() / 2; i++) {
            ArrayList<Post> temp = new ArrayList<Post>();
            temp.add(postsArr.get(i * 2));
            temp.add(postsArr.get(i * 2 + 1));
            twoPostsAsAGroup.add(temp);
        }
        if (postsArr.size() > postsArr.size() / 2 * 2) {
            ArrayList<Post> temp = new ArrayList<Post>();
            temp.add(postsArr.get(postsArr.size() - 1));
            twoPostsAsAGroup.add(temp);
        }
        this.postsArr = postsArr;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.community_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView1.setText(twoPostsAsAGroup.get(position).get(0).getBody_text());
        String UID1 = twoPostsAsAGroup.get(position).get(0).getUserID();
        // the following listener will read the current user's username
        realtimeDB.child("Users").child(UID1).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    holder.userName1.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        holder.likeNum1.setText(String.valueOf(twoPostsAsAGroup.get(position).get(0).getLikedNum()));
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(
                twoPostsAsAGroup.get(position).get(0).getImage_path()
        );

        // here for setting up the like feature
        holder.setLike_lists1(twoPostsAsAGroup.get(position).get(0).getUser_liked());

        //check if the current user liked the post set likeBtn1 to red if yes and grey otherwise
        holder.thumbUp1.setColorFilter(holder.getLike_lists1().contains(
                FirebaseAuth.getInstance().getUid()
            ) ? Color.RED:Color.GRAY);


        storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imageView1);
            }
        });
        //The following is for storing post key for transfering to posting detail
        holder.setPostKey1(twoPostsAsAGroup.get(position).get(0).getPostKey());

        if (2 * (position + 1) <= postsArr.size()) {
            holder.textView2.setText(twoPostsAsAGroup.get(position).get(1).getBody_text());
            String UID2 = twoPostsAsAGroup.get(position).get(0).getUserID();
            // the following listener will read the current user's username
            realtimeDB.child("Users").child(UID1).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        holder.userName2.setText(String.valueOf(task.getResult().getValue()));
                    }
                }
            });
            holder.likeNum2.setText(String.valueOf(twoPostsAsAGroup.get(position).get(1).getLikedNum()));
            StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child(
                    twoPostsAsAGroup.get(position).get(1).getImage_path()
            );
            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(holder.imageView2);
                }
            });
            //The following is for storing post key for transfering to posting detail
            holder.setPostKey2(twoPostsAsAGroup.get(position).get(1).getPostKey());

            // here for setting up the like feature
            holder.setLike_lists2(twoPostsAsAGroup.get(position).get(1).getUser_liked());

            //check if the current user liked the post set likeBtn1 to red if yes and grey otherwise
            holder.thumbUp2.setColorFilter(holder.getLike_lists2().contains(
                    FirebaseAuth.getInstance().getUid()
            ) ? Color.RED:Color.GRAY);
        }
        if (position == twoPostsAsAGroup.size() - 1 && twoPostsAsAGroup.get(twoPostsAsAGroup.size() - 1).size() == 1) {
            holder.imageView2.setVisibility(View.GONE);
            holder.thumbUp2.setVisibility(View.GONE);
            holder.acc2.setVisibility(View.GONE);
            holder.textView2.setVisibility(View.GONE);
            holder.likeNum2.setVisibility(View.GONE);
        }

        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("postID", holder.getPostKey1());
                context.startActivity(intent);
            }
        });
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("postID", holder.getPostKey2());
                context.startActivity(intent);
            }
        });

        holder.thumbUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID = FirebaseAuth.getInstance().getUid();
                DatabaseReference post_ref = FirebaseDatabase.getInstance().getReference().child("posts/"+ holder.getPostKey1());
                DatabaseReference post_like_ref = post_ref.getRef().child("user_liked");
                if (holder.getLike_lists1().isEmpty()){
                    holder.appendLikedUser1(UID);
                    post_like_ref.push();
                    post_like_ref.setValue(holder.getLike_lists1());
                }
                else if(holder.getLike_lists1().contains(UID)){
                    holder.getLike_lists1().remove(UID);
                    post_like_ref.setValue(holder.getLike_lists1());
                }else{
                    holder.appendLikedUser1(UID);
                    post_like_ref.setValue(holder.getLike_lists1());
                }
            }
        });

        holder.thumbUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID = FirebaseAuth.getInstance().getUid();
                DatabaseReference post_ref = FirebaseDatabase.getInstance().getReference().child("posts/"+ holder.getPostKey2());
                DatabaseReference post_like_ref = post_ref.getRef().child("user_liked");
                if (holder.getLike_lists2().isEmpty()){
                    holder.appendLikedUser2(UID);
                    post_like_ref.push();
                    post_like_ref.setValue(holder.getLike_lists2());
                }
                else if(holder.getLike_lists2().contains(UID)){
                    holder.getLike_lists2().remove(UID);
                    post_like_ref.setValue(holder.getLike_lists2());
                }else{
                    holder.appendLikedUser1(UID);
                    post_like_ref.setValue(holder.getLike_lists2());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return twoPostsAsAGroup == null ? 0 : twoPostsAsAGroup.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView acc2;
        private ImageView thumbUp1, thumbUp2;
        private TextView userName1;
        private TextView userName2;
        private String postKey1;
        private String postKey2;
        private DatabaseReference like_ref1, like_ref2;
        private ArrayList<String> like_lists1, like_lists2;
        private TextView likeNum1;
        private TextView likeNum2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.community_item_text_1);
            textView2 = itemView.findViewById(R.id.community_item_text_2);
            imageView1 = itemView.findViewById(R.id.community_item_img_1);
            imageView2 = itemView.findViewById(R.id.community_item_img_2);
            acc2 = itemView.findViewById(R.id.community_item_acc_icon_2);
            thumbUp2 = itemView.findViewById(R.id.community_item_thumb_up_2);
            userName1 = itemView.findViewById(R.id.community_item_username_1);
            userName2 = itemView.findViewById(R.id.community_item_username_2);
            likeNum1 = itemView.findViewById(R.id.community_like_num1);
            likeNum2 = itemView.findViewById(R.id.community_like_num2);
            thumbUp1 = itemView.findViewById(R.id.community_item_thumb_up_1);
        }

        public DatabaseReference getLike_ref1() {
            return like_ref1;
        }

        public void setLike_ref1(DatabaseReference like_ref1) {
            this.like_ref1 = like_ref1;
        }

        public DatabaseReference getLike_ref2() {
            return like_ref2;
        }

        public void setLike_ref2(DatabaseReference like_ref2) {
            this.like_ref2 = like_ref2;
        }

        public ArrayList<String> getLike_lists1() {
            return like_lists1;
        }

        public void setLike_lists1(ArrayList<String> like_lists1) {
            this.like_lists1 = like_lists1;
        }

        public ArrayList<String> getLike_lists2() {
            return like_lists2;
        }

        public void setLike_lists2(ArrayList<String> like_lists2) {
            this.like_lists2 = like_lists2;
        }

        public void appendLikedUser1(String UID) {
            this.like_lists1.add(UID);
        }


        public void appendLikedUser2(String UID) {
            this.like_lists2.add(UID);
        }


        public String getPostKey1() {
            return postKey1;
        }

        public void setPostKey1(String postKey1) {
            this.postKey1 = postKey1;
        }

        public String getPostKey2() {
            return postKey2;
        }

        public void setPostKey2(String postKey2) {
            this.postKey2 = postKey2;
        }
    }
}