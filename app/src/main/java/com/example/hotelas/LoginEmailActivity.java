package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelas.databinding.ActivityLoginEmailBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.service.auth.AuthService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LoginEmailActivity extends AppCompatActivity {

    private ActivityLoginEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityLoginEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up onClickListener for continueEmail button
        binding.continueEmail.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();

            // Validate email format
            if (!isEmailValid(email)) {
                binding.emailEditText.setError("Invalid email format");
                binding.emailEditText.requestFocus();
                return;
            }

            // Check if user is registered
            isUserRegistered(email);
        });
    }

    private void isUserRegistered (String email) {
        AuthService authService = new AuthService();

        authService.checkEmailExists(email ,new AuthService.CallBack() {
            @Override
            public void onSuccess(ApiResponse<Boolean> result) {
                if (result.getResult()) {
                    // Navigate to password entry
                    Intent intent = new Intent(LoginEmailActivity.this, EnterPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    // Navigate to registration
                    Intent intent = new Intent(LoginEmailActivity.this, RegisterActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
