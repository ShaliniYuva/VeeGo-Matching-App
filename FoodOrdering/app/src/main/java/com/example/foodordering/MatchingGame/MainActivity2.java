package com.example.foodordering.MatchingGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.LoginActivity;
import com.example.foodordering.R;

public class MainActivity2 extends AppCompatActivity {

    // Declare the ImageViews directly
    private ImageView memoryMatchArrow;
    private ImageView loginArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the activity layout
        setContentView(R.layout.activity_main2);

        // Initialize the ImageViews by finding them by ID
        memoryMatchArrow = findViewById(R.id.memoryMatchArrow);
        loginArrow = findViewById(R.id.loginArrow);

        // Set click listeners for both arrows
        setClickListeners();
    }

    // Method to set listeners for both arrow buttons
    private void setClickListeners() {
        // Navigate to Memory Match when clicking the memory match arrow
        memoryMatchArrow.setOnClickListener(v -> openMemoryMatch());

        // Handle click for the Login Arrow
        loginArrow.setOnClickListener(v -> openLoginActivity());
    }

    // Method to open the MemoryMatchActivity
    private void openMemoryMatch() {
        // Create an Intent to open the MemoryMatchActivity
        Intent intent = new Intent(MainActivity2.this, MemoryMatchActivity.class);

        // Start the MemoryMatchActivity
        startActivity(intent);
    }

    // Method to open the LoginActivity
    private void openLoginActivity() {
        // Create an Intent to open the LoginActivity
        Intent intent = new Intent(MainActivity2.this, LoginActivity.class);

        // Add flags to clear the previous activities in the stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the LoginActivity
        startActivity(intent);
    }
}
