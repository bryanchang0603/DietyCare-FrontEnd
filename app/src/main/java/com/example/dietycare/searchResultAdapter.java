package com.example.dietycare;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class searchResultAdapter extends RecyclerView.Adapter<searchResultAdapter.Holder> {

    private ArrayList<Food> foodArrayList;
    private Context context;

    public searchResultAdapter(ArrayList<Food> foodArrayList, Context context) {
        this.foodArrayList = foodArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_recyclerview, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView.setText(foodArrayList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return foodArrayList == null ? 0 : foodArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView textView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
        }
    }
}
