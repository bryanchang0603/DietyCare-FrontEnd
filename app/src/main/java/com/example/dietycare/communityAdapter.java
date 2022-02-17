package com.example.dietycare;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class communityAdapter extends RecyclerView.Adapter<communityAdapter.Holder> {

    private ArrayList<Post> postsArr;
    private Context context;
    ArrayList<ArrayList<Post>> twoPostsAsAGroup;

    public communityAdapter(ArrayList<Post> postsArr, Context context) {
        twoPostsAsAGroup = new ArrayList<ArrayList<Post>>();
        for (int i = 0; i < postsArr.size()/2; i++) {
            ArrayList<Post> temp = new ArrayList<Post>();
            temp.add(postsArr.get(i*2));
            temp.add(postsArr.get(i*2+1));
            twoPostsAsAGroup.add(temp);
        }
        if (postsArr.size() > postsArr.size()/2*2) {
            ArrayList<Post> temp = new ArrayList<Post>();
            temp.add(postsArr.get(postsArr.size()-1));
            twoPostsAsAGroup.add(temp);
        }
        this.postsArr= postsArr;
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
        holder.userName1.setText(twoPostsAsAGroup.get(position).get(0).getUserID());
        
        if (2*(position+1) <= postsArr.size()) {
            holder.textView2.setText(twoPostsAsAGroup.get(position).get(1).getBody_text());
            holder.userName2.setText(twoPostsAsAGroup.get(position).get(1).getUserID());
        }
        if (position == twoPostsAsAGroup.size()-1 && twoPostsAsAGroup.get(twoPostsAsAGroup.size()-1).size() == 1) {
            holder.imageView2.setVisibility(View.GONE);
            holder.thumbUp2.setVisibility(View.GONE);
            holder.acc2.setVisibility(View.GONE);
            holder.textView2.setVisibility(View.GONE);
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // jump to next page here
//            }
//        });
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
        private ImageView thumbUp2;
        private TextView userName1;
        private TextView userName2;

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
        }
    }
}