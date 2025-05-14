package com.example.hotelas;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityStartBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.service.auth.AuthService;
import com.example.hotelas.service.auth.googleAuth.GoogleSignInService;
import com.example.hotelas.service.callback.ServiceExecutor;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    private GoogleSignInService googleSignInService;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PrefManager prefManager = new PrefManager(this);
        AuthenticationResponse user = prefManager.getAuthResponse();
        if (user != null && user.isValid()) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.loginGmail.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        googleSignInService = new GoogleSignInService(StartActivity.this);
        authService = new AuthService();

        binding.loginGoogle.setOnClickListener(v -> {
            googleSignInService.signIn();
        });

        binding.buttonExplore.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == googleSignInService.getRequestCode()) {
            googleSignInService.handleSignInResult(data, new GoogleSignInService.SignInCallback() {
                @Override
                public void onSuccess(String serverAuthCode) {
                    // Gửi serverAuthCode về server
                    authService.outBoundAuthentication(serverAuthCode, new ServiceExecutor.CallBack<AuthenticationResponse>() {
                        @Override
                        public void onSuccess(ApiResponse<AuthenticationResponse> result) {

                            // người dũng cũ
                            if (result.getResult().isValid()) {
                                // đăng nhập thành công
                                Toast.makeText(StartActivity.this,
                                        "Đăng nhập thành công!",
                                        Toast.LENGTH_SHORT).show();

                                // lưu thông tin đăng nhập
                                PrefManager prefManager = new PrefManager(StartActivity.this);
                                prefManager.saveAuthResponse(result.getResult());

                                // chuyển đến main activity
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                // đăng nhập thành công
                                Toast.makeText(StartActivity.this,
                                        "Chào mừng bạn đến với hệ thống đặt phòng Hotelas!",
                                        Toast.LENGTH_SHORT).show();

                                // lưu thông tin đăng nhập
                                PrefManager prefManager = new PrefManager(StartActivity.this);
                                prefManager.saveAuthResponse(AuthenticationResponse.builder()
                                        .accessToken(result.getResult().getAccessToken())
                                        .build());

                                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                                intent.putExtra("name", result.getResult().getName());
                                intent.putExtra("accessToken", result.getResult().getAccessToken());
                                intent.putExtra("email", result.getResult().getEmail());
                                intent.putExtra("imageUrl", result.getResult().getImageUrl());
                                intent.putExtra("isActive", result.getResult().isActive());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Đăng nhập thất bại
                            Toast.makeText(StartActivity.this,
                                            "Đăng nhập thất bại: " + errorMessage,
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("SignInError", errorMessage);
                }
            });
        }
    }
}