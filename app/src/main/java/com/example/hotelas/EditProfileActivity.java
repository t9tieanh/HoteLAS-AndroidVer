package com.example.hotelas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.ActivityEditProfileBinding;
import com.example.hotelas.model.request.user.CustomerRequestDTO;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.user.UserService;
import com.example.hotelas.utils.ImageUtils;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private PrefManager prefManager;

    private UserService userService;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                // viết api cập nhật ảnh tại đây
                                try {
                                    userService.updateUserImage(EditProfileActivity.this, ImageUtils.createPartFromUri(EditProfileActivity.this,"file",uri), new ServiceExecutor.CallBack<CreationResponse>() {
                                        @Override
                                        public void onSuccess(ApiResponse<CreationResponse> result) {
                                            Toast.makeText(EditProfileActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();
                                            // Load avatar mặc định bằng Glide
                                            Glide.with(EditProfileActivity.this)
                                                    .load(FileContant.FILE_API_URL + result.getResult().getId())
                                                    .error(R.drawable.defaultprofileimage)
                                                    .into(binding.imgAvatar);

                                            // lưu lại thông tin ảnh
                                            prefManager.updateAuthField(PrefManager.AuthField.IMAGE, result.getResult().getId());
                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {
                                            Toast.makeText(EditProfileActivity.this, "Cập nhật ảnh thất bại: " + errorMessage,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        userService = new UserService(prefManager.getAuthResponse().getAccessToken());

        setupToolbar();
        setupListeners();
        loadUserInfo();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupListeners() {
        // 2. Bấm vào nút camera để mở gallery
        binding.btnEditAvatar.setOnClickListener(v -> {
            // Chỉ chọn file image/*
            pickImageLauncher.launch("image/*");
        });

        binding.btnSaveChange.setOnClickListener(v -> {
            saveProfileChanges();
        });
    }

    private void loadUserInfo() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("user_name");
        String userEmail = intent.getStringExtra("user_email");
        String userPhone = intent.getStringExtra("user_phone");
        String imgUrl = intent.getStringExtra("image_url");

        // Ví dụ load dữ liệu có sẵn
        binding.tvUserName.setText(prefManager.getAuthResponse().getUsername());
        binding.tvEmail.setText(userEmail);

        binding.edtEmail.setText(userEmail);
        binding.edtFullName.setText(userName);
        binding.edtPhone.setText(userPhone);

        // Load avatar mặc định bằng Glide
        Glide.with(this)
                .load(FileContant.FILE_API_URL + imgUrl)
                .error(R.drawable.defaultprofileimage)
                .into(binding.imgAvatar);
    }

    private void saveProfileChanges() {
        String email = binding.edtEmail.getText().toString().trim();
        String fullName = binding.edtFullName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();

        if (email.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: gọi API hoặc lưu local
        CustomerRequestDTO customer = CustomerRequestDTO.builder()
                .name(binding.edtFullName.getText().toString())
                .phone(binding.edtPhone.getText().toString())
                .email(binding.edtEmail.getText().toString())
                .build();

        userService.editCustomerProfile(customer, new ServiceExecutor.CallBack<CustomerResponseDTO>() {
            @Override
            public void onSuccess(ApiResponse<CustomerResponseDTO> result) {
                Toast.makeText(EditProfileActivity.this, "Cập nhật profile thành công !",Toast.LENGTH_SHORT).show();

                // update pref
                prefManager.updateAuthField(PrefManager.AuthField.EMAIL, result.getResult().getEmail());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditProfileActivity.this, "Cập nhật profile thất bại: " + errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
