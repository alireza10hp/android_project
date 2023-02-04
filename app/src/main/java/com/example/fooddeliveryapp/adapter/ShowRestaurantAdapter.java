package com.example.fooddeliveryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.ShowRestaurantFood;
import com.example.fooddeliveryapp.model.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class ShowRestaurantAdapter extends RecyclerView.Adapter<ShowRestaurantAdapter.viewHolder> {

    private Context mContext;
    private String userName;
    private List<RestaurantModel> mRestaurantRestaurantList = new ArrayList<>();

    public ShowRestaurantAdapter(Context mContext, List<RestaurantModel> mRestaurantRestaurantList, String userName) {
        this.mContext = mContext;
        this.mRestaurantRestaurantList = mRestaurantRestaurantList;
        this.userName = userName;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_restaurant, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        RestaurantModel model = mRestaurantRestaurantList.get(position);
        holder.tv_restName.setText(model.getUsername());
        if (model.getImageUrl().equals("default")) {
            holder.iv_restImage.setImageResource(R.drawable.restaurant);
        } else {
            Glide.with(mContext).load(model.getImageUrl()).into(holder.iv_restImage);
        }
        holder.ll_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowRestaurantFood.class);
                intent.putExtra("restName", model.getUsername());
                intent.putExtra("userName", userName);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRestaurantRestaurantList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_restImage;
        private TextView tv_restName;
        private LinearLayout ll_restaurant;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            iv_restImage = itemView.findViewById(R.id.iv_restImage);
            tv_restName = itemView.findViewById(R.id.tv_restName);
            ll_restaurant = itemView.findViewById(R.id.ll_restaurant);
        }
    }
}
