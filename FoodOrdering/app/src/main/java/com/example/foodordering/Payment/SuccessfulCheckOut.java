package com.example.foodordering.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.Activity.MainActivity;
import com.example.foodordering.databinding.ActivityCheckoutSuccessBinding;

public class SuccessfulCheckOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the binding object
        ActivityCheckoutSuccessBinding layoutBinding = ActivityCheckoutSuccessBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        // Set up the "Return to Home" button
        Button returnHomeButton = layoutBinding.btnReturnHome;
        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main screen (Home screen)
                Intent intent = new Intent(SuccessfulCheckOut.this, MainActivity.class);
                startActivity(intent);
                finish();  // Optional: finish this activity to prevent going back to it with the back button
            }
        });
    }
}
