package com.example.foodordering.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodordering.Domain.Foods;
import com.example.foodordering.Helper.ManagementCart;
import com.example.foodordering.Helper.ManagementFav;
import com.example.foodordering.R;
import com.example.foodordering.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagementCart managementCart;
    private ManagementFav managementFavourite;
    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managementCart = new ManagementCart(this);
        managementFavourite = new ManagementFav(this);

        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(this)
                .load(object.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.pic);

        binding.priceTxt.setText("RM" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText("RM" + (num * object.getPrice()));

        binding.plusBtn.setOnClickListener(v -> {
            num++;
            binding.numTxt.setText(String.valueOf(num));
            binding.totalTxt.setText("RM" + (num * object.getPrice()));
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num--;
                binding.numTxt.setText(String.valueOf(num));
                binding.totalTxt.setText("RM" + (num * object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managementCart.insertFood(object);
        });

        // Set initial favorite state
        isFavourite = managementFavourite.isFavourite(object);
        updateFavouriteIcon();

        // Click listener for favorite button
        binding.faveBtn.setOnClickListener(v -> {
            if (isFavourite) {
                managementFavourite.removeFood(object);
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
            } else {
                managementFavourite.addFood(object);
                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
            }
            isFavourite = !isFavourite;
            updateFavouriteIcon();
        });
    }

    private void updateFavouriteIcon() {
        int favIcon = isFavourite ? R.drawable.favorite : R.drawable.favorite;
        binding.faveBtn.setImageResource(favIcon);
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}
