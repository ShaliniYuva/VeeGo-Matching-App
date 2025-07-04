package com.example.foodordering.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodordering.Adapter.CartAdapter;
import com.example.foodordering.Helper.ChangeNumberItemsListener;
import com.example.foodordering.Helper.ManagementCart;
import com.example.foodordering.Payment.SuccessfulCheckOut;
import com.example.foodordering.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagementCart(this);

        setVariable();
        setCheckOutButton();
        calculateCart();
        initCartList();
    }

    private void initCartList() {
        if(managementCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);

        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(),managementCart , () -> calculateCart()));
    }

    private void calculateCart() {
        double percentTax = 0.02;  // 2% tax
        double packaging = managementCart.getListCart().isEmpty() ? 0 : 4; // Set packaging fee to 0 if cart is empty
        double tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + packaging) * 100) / 100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("RM" + itemTotal);
        binding.taxTxt.setText("RM" + tax);
        binding.packagingTxt.setText("RM" + packaging);
        binding.totalTxt.setText("RM" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this,MainActivity.class)));
    }

    private void setCheckOutButton() {
        binding.checkOutBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, SuccessfulCheckOut.class)));
    }
}

