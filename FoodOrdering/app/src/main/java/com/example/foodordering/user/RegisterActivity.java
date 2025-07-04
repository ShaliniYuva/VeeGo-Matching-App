package com.example.foodordering.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.LoginActivity;
import com.example.foodordering.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput, phoneInput;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, phoneLayout;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);

        usernameLayout = findViewById(R.id.usernameInputLayout);
        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        phoneLayout = findViewById(R.id.phoneInputLayout);

        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        registerButton.setOnClickListener(v -> validateAndRegister());

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateAndRegister() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        boolean isValid = true;

        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        phoneLayout.setError(null);

        if (username.isEmpty() || !username.matches("^[a-zA-Z0-9]+$")) {
            usernameLayout.setError("Username must contain letters and numbers only");
            isValid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            isValid = false;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        }

        // Check if the password contains only letters, numbers, @ or #
        if (!password.matches("^[a-zA-Z0-9@#]+$")) {
            passwordLayout.setError("Password can only contain letters, numbers, @, and #");
            isValid = false;
        }

        if (phone.isEmpty()) {
            phoneLayout.setError("Phone number is required");
            isValid = false;
        } else if (!phone.matches("^01[0-9]{8,9}$")) {
            phoneLayout.setError("Invalid phone number");
            isValid = false;
        }

        // Check if phone number is not empty and has invalid length (not between 10 and 11 digits)
        if (!phone.isEmpty() && (phone.length() < 10 || phone.length() > 11)) {
            Toast.makeText(this, "Phone number must have 10 to 11 digits", Toast.LENGTH_SHORT).show();
            phoneLayout.setError("Invalid phone number");
            isValid = false;
        }

        // Check if all fields are empty and show a Toast message
        if (username.isEmpty() && email.isEmpty() && password.isEmpty() && phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            registerUser(username, email, password, phone);
        }
    }



    private void registerUser(String username, String email, String password, String phone) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            User newUser = new User(username, email, phone);
                            databaseReference.child(userId).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            runOnUiThread(() ->
                                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                            );

                                            new android.os.Handler().postDelayed(() -> {
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            }, 2000);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Check if the error is due to email already being in use
                        if (task.getException() != null && task.getException().getMessage().contains("The email address is already in use")) {
                            Toast.makeText(RegisterActivity.this, "This email is already registered. Please try a different one.", Toast.LENGTH_SHORT).show();
                        }
                        // If the email is incorrect or doesn't exist in the Firebase system
                        else if (task.getException() != null && task.getException().getMessage().contains("The email address is badly formatted")) {
                            Toast.makeText(RegisterActivity.this, "The email format is invalid. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                        }
                        // Handle other types of errors (e.g., weak passwords)
                        else {
                            Toast.makeText(RegisterActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static class User {
        public String username, email, phone;

        public User() {
        }

        public User(String username, String email, String phone) {
            this.username = username;
            this.email = email;
            this.phone = phone;
        }
    }
}
