package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotelas.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get email from intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");


        // Set click listener for the register button
        binding.createAndLoginButton.setOnClickListener(v -> {
            if (email != null) {
                createUser(email);
            }
        });
    }

    private void createUser(String email) {
        String password = binding.passwordEditText.getText().toString();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString();

        // Validate password and confirm password
        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError("Password cannot be empty");
            binding.passwordEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            binding.confirmPasswordEditText.setError("Confirm password cannot be empty");
            binding.confirmPasswordEditText.requestFocus();
            return;
        } else if (password.length() < 8) {
            binding.passwordEditText.setError("Password must be at least 8 characters long");
            binding.passwordEditText.requestFocus();
            return;
        } else if (!password.matches(".*\\d.*")) {
            binding.passwordEditText.setError("Password must contain at least one number");
            binding.passwordEditText.requestFocus();
            return;
        } else if (!password.equals(confirmPassword)) {
            binding.confirmPasswordEditText.setError("Passwords do not match");
            binding.confirmPasswordEditText.requestFocus();
            return;
        }

        // Create user with email and password
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Registration success, user is created
//                        Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
//                        startActivity(mainActivityIntent);
//                        finish();
//                    } else {
//                        // Registration failed
//                        Toast.makeText(RegisterActivity.this,
//                                "Registration failed: " + task.getException().getMessage(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
