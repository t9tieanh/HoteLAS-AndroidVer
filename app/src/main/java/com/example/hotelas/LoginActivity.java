package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityLoginBinding;
import com.example.hotelas.model.request.AuthenticationRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.service.auth.AuthService;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = new AuthService();

        // Nút "Đăng nhập ngay"
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            login(email, password);
        });

        binding.tvLoginNow.setOnClickListener(v -> {
            back();
        });


        binding.btnBack.setOnClickListener(v -> {
           back();
        });
    }

    private void login (String email, String password) {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(email)
                .password(password)
                .build();

        authService.login(request ,new AuthService.CallBack<AuthenticationResponse>() {
            @Override
            public void onSuccess(ApiResponse<AuthenticationResponse> result) {
                // Navigate to password entry
                PrefManager prefManager = new PrefManager(LoginActivity.this);
                prefManager.saveAuthResponse(result.getResult());

                // chuyen activity
                // transfer activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back () {
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
