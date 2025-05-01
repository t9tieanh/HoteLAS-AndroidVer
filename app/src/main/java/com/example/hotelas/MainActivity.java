package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityMainBinding;
import com.example.hotelas.model.response.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // full screen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setSupportActionBar(binding.toolbar);

        // Hiển thị fragment mặc định
        SearchFragment searchFragment = new SearchFragment();
        replaceFragment(searchFragment);

        prefManager = new PrefManager(this);

        // Nếu đã đăng nhập thì show BottomNav và gán listener
        if (prefManager.isLoggedIn()) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                } else if (item.getItemId() == R.id.search) {
                    replaceFragment(new SearchFragment());
                } else if (item.getItemId() == R.id.reservations) {
                    // replaceFragment(new ReservationsFragment());
                }
                return true;
            });
        // Nếu chưa đăng nhập thì ẩn BottomNav đi
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
        }

        updateToolbarGreeting();
    }

    private void updateToolbarGreeting() {
        if (prefManager.isLoggedIn()) {
            AuthenticationResponse auth = prefManager.getAuthResponse();
            String name = auth != null ? auth.getUsername() : "Bạn";
            binding.tvToolbarTitle.setText("Chào mừng bạn đã đến với hệ thống đặt phòng HoteAS");
            binding.tvToolbarSubtitle.setText("Xin chào " + name);
        } else {
            binding.tvToolbarTitle.setText("Đăng nhập để khám phá thêm");
            binding.tvToolbarSubtitle.setText("Xin chào quý khách");
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.btnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}