package com.example.dietycare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietycare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerHolder>{

    Context context;
    List<String> follower;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @NonNull
    @Override
    public FollowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_user,parent, false);
        FollowerAdapter.FollowerHolder holder = new FollowerAdapter.FollowerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerHolder holder, int position) {
        //set username
        database.getReference().child("Users").child(follower.get(position)).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    holder.username.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        holder.setFollower(follower.get(position));

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // current getting current User UID, may need to change to parameter passed from follow activity

                //remove that user from current user's follower list
                Task<Void> remove_following = database.getReference().child("follower").child(UID).child(holder.getFollower()).removeValue();
                remove_following.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"User Banned",Toast.LENGTH_SHORT).show();
                    }
                });

                //removing current user from that user's following list
                Task<Void> remove_follower = database.getReference().child("following").child(holder.getFollower()).child(UID).removeValue();
            }
        });
    }

    @Override
    public int getItemCount()  {
        return follower.size();
    }

    public FollowerAdapter(Context context, List<String> following) {
        this.context = context;
        this.follower = following;
    }

    public class FollowerHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView user_image;
        private Button unfollow;
        private String followerID; // this is for store other user's UID

        public FollowerHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.follower_user_name);
            user_image = itemView.findViewById(R.id.follower_user_image);
            unfollow = itemView.findViewById(R.id.follower_ban);
            followerID = null;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public ImageView getUser_image() {
            return user_image;
        }

        public void setUser_image(ImageView user_image) {
            this.user_image = user_image;
        }

        public Button getUnfollow() {
            return unfollow;
        }

        public void setUnfollow(Button unfollow) {
            this.unfollow = unfollow;
        }

        public String getFollower() {
            return followerID;
        }

        public void setFollower(String follower) {
            this.followerID = follower;
        }
    }
}
