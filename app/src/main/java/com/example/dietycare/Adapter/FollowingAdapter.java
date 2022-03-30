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

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingHolder>{

    Context context;
    List<String> following;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @NonNull
    @Override
    public FollowingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_user,parent, false);
        FollowingHolder holder = new FollowingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingHolder holder, int position) {
        //set username
        database.getReference().child("Users").child(following.get(position)).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
        holder.setFollowing(following.get(position));

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // current getting current User UID, may need to change to parameter passed from follow activity

                //remove that user from current user's following list
                Task<Void> remove_following = database.getReference().child("following").child(UID).child(holder.getFollowing()).removeValue();
                remove_following.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"User Unfollowed",Toast.LENGTH_SHORT).show();
                    }
                });

                //removing current user from that user's followed list
                Task<Void> remove_follower = database.getReference().child("follower").child(holder.getFollowing()).child(UID).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return following.size();
    }

    public FollowingAdapter(Context context, List<String> following) {
        this.context = context;
        this.following = following;
    }

    public class FollowingHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private ImageView user_image;
        private Button unfollow;
        private String followingID; // this is for store other user's UID
        public FollowingHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.following_user_name);
            user_image = itemView.findViewById(R.id.following_user_image);
            unfollow = itemView.findViewById(R.id.following_unfollow);
            followingID = null;
        }

        public Button getUnfollow() {
            return unfollow;
        }

        public void setUnfollow(Button unfollow) {
            this.unfollow = unfollow;
        }

        public String getFollowing() {
            return followingID;
        }

        public void setFollowing(String following) {
            this.followingID = following;
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
    }
}
