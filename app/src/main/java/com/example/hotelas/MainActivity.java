package com.example.hotelas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hotelas.config.PaymentPrefManager;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityMainBinding;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private PrefManager prefManager;
    private PaymentPrefManager paymentPrefManager;


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
        paymentPrefManager = new PaymentPrefManager(this);

        // Nếu đã đăng nhập thì show BottomNav và gán listener
        if (prefManager.isLoggedIn()) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                } else if (item.getItemId() == R.id.search) {
                    replaceFragment(new SearchFragment());
                } else if (item.getItemId() == R.id.reservations) {
                    Intent intent = new Intent(this, ReservationHistoryActivity.class);
                    startActivity(intent);
                }
                return true;
            });
        // Nếu chưa đăng nhập thì ẩn BottomNav đi
        } else {
            binding.bottomNavigationView.setVisibility(View.GONE);
        }

        updateToolbarGreeting();

        // phát cảnh bao payment nếu có đơn đặt phòng chưa hoàn thành
        showPaymentAlert();
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

    private void showPaymentAlert () {
        if (!paymentPrefManager.isPaymentExpired()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);  // nếu ở Fragment thì dùng getContext()

            builder.setTitle("🔔 Một đơn đặt phòng chưa hoàn tất")
                    .setMessage("Bạn còn một đơn đặt phòng chưa thanh toán. Bạn có muốn tiếp tục ngay không?")
                    .setCancelable(false)  // Không thể tắt bằng nút back hoặc bấm ngoài
                    .setPositiveButton("Thanh toán ngay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Chuyển sang PaymentActivity
                            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();  // Đóng dialog
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}