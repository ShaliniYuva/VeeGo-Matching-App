package com.example.foodordering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodordering.Domain.Foods;
import com.example.foodordering.Helper.ManagementFav;
import com.example.foodordering.R;
import com.example.foodordering.user.FavouriteActivity;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Foods> favouriteList;
    private ManagementFav managementFavourite;

    public FavouriteAdapter(Context context, ArrayList<Foods> favouriteList, ManagementFav managementFavourite) {
        this.context = context;
        this.favouriteList = favouriteList;
        this.managementFavourite = managementFavourite;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = favouriteList.get(position);

        holder.title.setText(food.getTitle());
        holder.price.setText("RM" + food.getPrice());

        Glide.with(context)
                .load(food.getImagePath())
                .into(holder.image);

        holder.removeBtn.setOnClickListener(v -> {
            managementFavourite.removeFood(food);
            favouriteList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show();
            if (context instanceof FavouriteActivity) {
                ((FavouriteActivity) context).loadFavourites();

            }

        });

    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView image, removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.foodTitle);
            price = itemView.findViewById(R.id.foodPrice);
            image = itemView.findViewById(R.id.foodImage);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}
