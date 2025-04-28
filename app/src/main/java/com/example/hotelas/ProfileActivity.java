package com.example.hotelas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.ActivityProfileBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.user.UserService;

public class ProfileActivity extends AppCompatActivity {

    // Declare ViewBinding
    private ActivityProfileBinding binding;
    private UserService userService;

    CustomerResponseDTO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        String token = new PrefManager(this).getAuthResponse().getAccessToken();
        userService = new UserService(token);
        getUserProfile();
    }

    private void setUserInfo (CustomerResponseDTO customer) {
        // Set data to Views using ViewBinding
        binding.tvUserName.setText(customer.getName());
        binding.tvUserEmail.setText(customer.getEmail());
        binding.tvValueName.setText(customer.getName());
        binding.tvValueEmail.setText(customer.getEmail());

        if (customer.getPhone().isEmpty()) {
            binding.tvValueMobile.setText("Chưa có");
        } else binding.tvValueMobile.setText(customer.getPhone());

        // Set loyalty points and jobs completed (Example, can be fetched dynamically)
        binding.tvLoyaltyPoints.setText(String.valueOf(customer.getLoyaltyPoints()));
        binding.tvJobsCompleted.setText("37");

        // Load image into ImageView using Glide
        Glide.with(this)
                .load(FileContant.FILE_API_URL + customer.getImgUrl()) // Replace with the actual image URL
                .into(binding.imgAvatar);
    }

    private void getUserProfile () {
        userService.getCustomerProfile(new ServiceExecutor.CallBack<CustomerResponseDTO>() {
            @Override
            public void onSuccess(ApiResponse<CustomerResponseDTO> result) {
                user = result.getResult();
                setUserInfo(user);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up the binding reference
        binding = null;
    }
}
