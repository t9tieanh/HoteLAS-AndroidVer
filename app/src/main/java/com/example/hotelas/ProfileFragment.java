package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.FragmentProfileBinding;
import com.example.hotelas.model.response.AuthenticationResponse;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUserInfo();
//        // Gán dữ liệu ví dụ
//        binding.textViewName.setText("Xin chào, Tiến Anh!");
    }

    private void setUserInfo () {
        PrefManager prefManager = new PrefManager(requireContext());
        AuthenticationResponse user = prefManager.getAuthResponse();

        if (user != null) {
            // lấy thông tin từ user
            if (user.getImageUrl() != null) {
                String imgUrl = FileContant.FILE_API_URL + user.getImageUrl();
                Glide.with(requireContext()).load(imgUrl)
                        .into(binding.profilePictureImageView);
            }
            binding.nameTextView.setText(user.getUsername());


            // tắt process
            binding.logoutButton.setOnClickListener(
                    v -> {logout();}
            );
            binding.loadingProgressBar.setVisibility(View.GONE);
        } else {
            logout();
        }
    }

    private void logout () {
        PrefManager prefManager = new PrefManager(requireContext());
        prefManager.clearAuthResponse();

        // transfer activity
        Intent intent = new Intent(requireContext(), StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
