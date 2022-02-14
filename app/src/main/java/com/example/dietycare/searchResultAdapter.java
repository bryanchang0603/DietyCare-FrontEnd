package com.example.dietycare;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
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
        Food food = foodArrayList.get(position);
        String foodName = food.getFoodName();
        double protein = food.getProtein();
        double fat = food.getFat();
        double carbo = food.getCarbo();
        double calorie = food.getCalorie();

        holder.textView.setText(foodArrayList.get(position).getFoodName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // jump to next page here
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("food name", foodName);
                intent.putExtra("protein", protein);
                intent.putExtra("fat", fat);
                intent.putExtra("carbo", carbo);
                intent.putExtra("calorie", calorie);
                context.startActivity(intent);

            }
        });
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
