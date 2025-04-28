package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.FragmentProfileBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.user.UserService;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserService userService;
    private CustomerResponseDTO user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate với ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo UserService với token
        String token = new PrefManager(requireContext())
                .getAuthResponse()
                .getAccessToken();
        userService = new UserService(token);

        // Gọi API để lấy profile
        getUserProfile();

        // Nút "Chỉnh sửa thông tin" (ví dụ)
        binding.btnSaveChange.setOnClickListener(v -> {
            updateProfile();
        });
        // Nút "Xem lịch sử đặt phòng"
        binding.btnViewHistory.setOnClickListener(v -> {
            // navigate tới lịch sử...
        });

        binding.btnLogout.setOnClickListener( v -> logout());
    }

    private void getUserProfile() {
        userService.getCustomerProfile(new ServiceExecutor.CallBack<CustomerResponseDTO>() {
            @Override
            public void onSuccess(ApiResponse<CustomerResponseDTO> result) {
                user = result.getResult();
                setUserInfo(user);
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(),
                                errorMessage,
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setUserInfo(CustomerResponseDTO customer) {
        binding.tvUserName.setText(customer.getName());
        binding.tvUserEmail.setText(customer.getEmail());
        binding.tvValueName.setText(customer.getName());
        binding.tvValueEmail.setText(customer.getEmail());

        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            binding.tvValueMobile.setText("Chưa có");
        } else {
            binding.tvValueMobile.setText(customer.getPhone());
        }

        binding.tvLoyaltyPoints.setText(
                String.valueOf(customer.getLoyaltyPoints()));
        binding.tvJobsCompleted.setText("37");  // hoặc giá trị thực từ API

        Glide.with(this)
                .load(FileContant.FILE_API_URL + customer.getImgUrl())
                .into(binding.imgAvatar);

        binding.btnLogout.setOnClickListener(v -> logout());
    }


    private void logout () {
        PrefManager prefManager = new PrefManager(requireContext());
        prefManager.clearAuthResponse();

        // transfer activity
        Intent intent = new Intent(requireContext(), StartActivity.class);
        startActivity(intent);
    }

    private void updateProfile () {
        // transfer activity
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);

        intent.putExtra("user_name", user.getName());
        intent.putExtra("user_email", user.getEmail());
        intent.putExtra("user_phone", user.getPhone());
        intent.putExtra("image_url",  user.getImgUrl());

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding
        binding = null;
    }
}
