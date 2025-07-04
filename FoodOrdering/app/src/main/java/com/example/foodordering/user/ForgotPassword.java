package com.example.foodordering.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.LoginActivity;
import com.example.foodordering.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private ImageView backBtnFP; // Declare the back button ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views by their ID
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backBtnFP = findViewById(R.id.backBtnFP); // Initialize the back button

        // Set up click listener for the back button
        backBtnFP.setOnClickListener(v -> navigateToLoginActivity());

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPassword.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send password reset email
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Reset password sent. Check your email.", Toast.LENGTH_LONG).show();
                                // Navigate to LoginActivity after successful password reset request
                                navigateToLoginActivity();
                            } else {
                                Toast.makeText(ForgotPassword.this, "Failed to send reset password. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    // Helper method to navigate to LoginActivity
    private void navigateToLoginActivity() {
        // Create an Intent to open the LoginActivity
        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);

        // Start the LoginActivity
        startActivity(intent);

        // Finish the current activity
        finish();
    }
}
