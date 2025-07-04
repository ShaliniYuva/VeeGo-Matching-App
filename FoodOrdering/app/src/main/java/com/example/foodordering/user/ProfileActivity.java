package com.example.foodordering.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.Activity.MainActivity;
import com.example.foodordering.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private EditText editUsername, editPhone, editPassword;
    private Button btnUpdate;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        editUsername = findViewById(R.id.editTextUsername);
        editPhone = findViewById(R.id.editTextPhone);
        editPassword = findViewById(R.id.editTextPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        backBtn = findViewById(R.id.backBtn3);
        progressBar = findViewById(R.id.progressBar); // Ensure you have a ProgressBar in your XML

        setVariable();

        btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void setVariable() {
        backBtn.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
    }

    private void updateProfile() {
        String newEmail = editUsername.getText().toString().trim();
        String newPhone = editPhone.getText().toString().trim();
        String newPassword = editPassword.getText().toString().trim();

        boolean emailValid = true, phoneValid = true, passwordValid = true;

        // Validate Email (if entered)
        if (!TextUtils.isEmpty(newEmail)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                editUsername.setError("Invalid email format");
                emailValid = false;
            }
        }

        // Validate Phone (if entered)
        if (!TextUtils.isEmpty(newPhone)) {
            if (!newPhone.matches("^01[0-9]{8,9}$")) {
                editPhone.setError("Invalid phone number format");
                phoneValid = false;
            } else if (newPhone.length() < 10 || newPhone.length() > 11) {
                editPhone.setError("Phone number must have 10 to 11 digits");
                phoneValid = false;
            }
        }

        // Validate Password (if entered)
        if (!TextUtils.isEmpty(newPassword)) {
            if (newPassword.length() < 6) {
                editPassword.setError("Password must be at least 6 characters");
                passwordValid = false;
            } else if (!newPassword.matches("^[a-zA-Z0-9@#]+$")) {
                editPassword.setError("Password can only contain letters, numbers, @, and #");
                passwordValid = false;
            }
        }

        // If no fields are entered, show a message
        if (TextUtils.isEmpty(newEmail) && TextUtils.isEmpty(newPhone) && TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "No changes detected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Stop if any entered field is invalid
        if (!emailValid || !phoneValid || !passwordValid) {
            Toast.makeText(this, "Please fix errors before updating", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Update Email (if valid)
        if (!TextUtils.isEmpty(newEmail) && emailValid) {
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    databaseReference.child("email").setValue(newEmail);
                    Toast.makeText(ProfileActivity.this, "Email updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Update Phone (if valid)
        if (!TextUtils.isEmpty(newPhone) && phoneValid) {
            databaseReference.child("phone").setValue(newPhone).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Phone number updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update phone number", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Update Password (if valid)
        if (!TextUtils.isEmpty(newPassword) && passwordValid) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        progressBar.setVisibility(View.GONE);
    }
}
