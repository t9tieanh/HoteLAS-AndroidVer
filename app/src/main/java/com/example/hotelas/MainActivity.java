package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityMainBinding;
import com.example.hotelas.model.response.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Ẩn ActionBar (nếu đang dùng AppCompatActivity)
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hiển thị fragment mặc định
        ProfileFragment profileStartFragment = new ProfileFragment();
        replaceFragment(profileStartFragment);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.favorites) {
                // replaceFragment(new FavoritesFragment());
            } else if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.reservations) {
                // replaceFragment(new ReservationsFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}