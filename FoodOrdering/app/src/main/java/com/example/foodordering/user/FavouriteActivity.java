package com.example.foodordering.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodordering.Activity.CartActivity;
import com.example.foodordering.Activity.MainActivity;
import com.example.foodordering.Adapter.FavouriteAdapter;
import com.example.foodordering.Domain.Foods;
import com.example.foodordering.Helper.ManagementFav;
import com.example.foodordering.databinding.ActivityFavouriteBinding;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private ActivityFavouriteBinding binding;
    private ManagementFav managementFavourite;
    private FavouriteAdapter adapter;
    private ArrayList<Foods> favouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementFavourite = new ManagementFav(this);

        setupRecyclerView();
        loadFavourites();

        setVariable();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteList = new ArrayList<>();
        adapter = new FavouriteAdapter(this, favouriteList, managementFavourite);
        binding.recyclerView.setAdapter(adapter);
    }

    public void loadFavourites() {
        favouriteList.clear(); // Clear the current list
        favouriteList.addAll(managementFavourite.getFavourites()); // Add updated items
        adapter.notifyDataSetChanged(); // Notify adapter of the change
    }

    public void removeFromFavourites(@NonNull Foods food) {
        managementFavourite.removeFood(food);
        Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
        loadFavourites();
    }
    private void setVariable() {
        binding.backBtn2.setOnClickListener(v -> startActivity(new Intent(FavouriteActivity.this, MainActivity.class)));
    }
}
