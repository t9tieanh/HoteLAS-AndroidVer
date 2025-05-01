package com.example.hotelas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityRegisterBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.service.auth.AuthService;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.user.UserService;
import com.example.hotelas.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserService userService;
    private PrefManager prefManager;

    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        String token = prefManager.getAuthResponse().getAccessToken();
        userService = new UserService(token);

        receiveIntentData();
        setupListeners();
    }

    private void setupListeners() {
        binding.createAndLoginButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString();
            String confirmPassword = binding.confirmPasswordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // thực hiện call
            userService.activeAccount(RegisterActivity.this, username, password, name, email,
                    ImageUtils.getBitmapFromImageView(binding.avatarImageView), new ServiceExecutor.CallBack<Boolean>() {
                        @Override
                        public void onSuccess(ApiResponse<Boolean> result) {
                            if (result.getResult()) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công cho " + username + "!", Toast.LENGTH_LONG).show();

                                // xóa access token
                                prefManager.clearAuthResponse();

                                // chuyển về lại login
                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void receiveIntentData() {
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (name != null) {
            binding.userNameDisplay.setText(name);
            binding.createPasswordText.setText("Xin chào " + name + "! Nhập username và password để tiếp tục.");
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Nếu có hình ảnh, load hình bằng Glide
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)  // Ảnh mặc định nếu chưa có
                    .error(R.drawable.defaultprofileimage)        // Ảnh lỗi nếu load fail
                    .centerCrop()
                    .into(binding.avatarImageView);          // <-- Phải thay TextView avatar thành ImageView mới load được
        }
    }
}
