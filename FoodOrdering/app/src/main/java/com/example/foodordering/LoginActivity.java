package com.example.foodordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.Activity.MainActivity;

import com.example.foodordering.MatchingGame.MainActivity2;
import com.example.foodordering.user.ForgotPassword;
import com.example.foodordering.user.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private TextInputLayout emailLayout, passwordLayout;
    private TextView forgotPassword, signUp;
    private Button loginButton;
    private ImageView backBtnMain;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Inflate the login layout

        // Initialize Firebase and Database Reference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // UI References (use findViewById instead of binding)
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signupLink);
        backBtnMain = findViewById(R.id.backtoMain1); // Initialize using findViewById

        // Button Click Listeners
        loginButton.setOnClickListener(v -> validateLogin());
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPassword.class)));
        signUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // Back Button to MainActivity2
        backBtnMain.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, com.example.foodordering.MatchingGame.MainActivity2.class)));
    }

    private void validateLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean isValid = true;

        // Reset previous errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        // Validate Email
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            isValid = false;
        }

        // Validate Password
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        } else if (!password.matches("[a-zA-Z0-9@#]+")) { // Check if password contains only allowed characters
            passwordLayout.setError("Password must contain only letters, numbers, @, or #");
            isValid = false;
        }

        // Proceed if valid
        if (isValid) {
            loginUser(email, password);
        }
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Log.d("LoginActivity", "Login successful for: " + user.getEmail());

                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            fetchUserData(user.getUid());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Email not registered
                    Toast.makeText(LoginActivity.this, "Email not registered, please register first.", Toast.LENGTH_SHORT).show();

                });
    }

    private void fetchUserData(String userId) {
        databaseReference.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.getValue(String.class);

                    Log.d("MainActivity", "Fetched username: " + username);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                    finish(); // Closes LoginActivity
                } else {
                    Log.e("MainActivity", "User data not found");
                    Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Database error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
