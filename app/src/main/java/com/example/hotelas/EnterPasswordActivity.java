package com.example.hotelas;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityEnterPasswordBinding;
import com.example.hotelas.model.request.AuthenticationRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.service.auth.AuthService;

public class EnterPasswordActivity extends AppCompatActivity {

    private ActivityEnterPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityEnterPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get email from intent
        String email = getIntent().getStringExtra("email");

        // Set the information text with the email
        if (email != null) {
            String informationText = "Please enter your password for " + email;
            binding.informationEmailText.setText(informationText);
        }

        // Set click listener for the login button
        binding.loginButton.setOnClickListener(v -> {
            String password = binding.passwordEditText.getText().toString();

            // Call the login method
            if (email != null) {
                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
        AuthService authService = new AuthService();
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(email)
                .password(password)
                .build();

        authService.login(request ,new AuthService.CallBack<AuthenticationResponse>() {
            @Override
            public void onSuccess(ApiResponse<AuthenticationResponse> result) {
                if (result.getResult() != null) {
                    // Navigate to password entry
                    PrefManager prefManager = new PrefManager(EnterPasswordActivity.this);
                    prefManager.saveAuthResponse(result.getResult());

                    // chuyen activity
                    // transfer activity
                    Intent intent = new Intent(EnterPasswordActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(EnterPasswordActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EnterPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
