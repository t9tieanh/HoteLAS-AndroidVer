package com.example.hotelas;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hotelas.adapter.ReservationHistoryAdapter;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityReservationHistoryBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.reservation.history.ReservationHistoryResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.reservation.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class ReservationHistoryActivity extends AppCompatActivity {

    private ActivityReservationHistoryBinding binding;

    private List<ReservationHistoryResponse> reservationLst = new ArrayList<>();
    private ReservationService reservationService;
    private PrefManager prefManager;
    private ReservationHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar setup
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Lịch sử đặt phòng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefManager = new PrefManager(this);
        reservationService = new ReservationService(prefManager.getAuthResponse().getAccessToken());

        // Simulate loading
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            loadReservationHistory();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadReservationHistory() {
        binding.progressBar.setVisibility(View.VISIBLE);

        reservationService.getReservationHistory(new ServiceExecutor.CallBack<List<ReservationHistoryResponse>>() {
            @Override
            public void onSuccess(ApiResponse<List<ReservationHistoryResponse>> result) {
                reservationLst = result.getResult();

                new Handler().postDelayed(() -> {

                    // Setup RecyclerView
                    adapter = new ReservationHistoryAdapter(ReservationHistoryActivity.this,reservationLst);
                    binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(ReservationHistoryActivity.this));
                    binding.recyclerViewResults.setAdapter(adapter);

                    binding.progressBar.setVisibility(View.GONE);

                    if (reservationLst.isEmpty()) {
                        binding.tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvResultCount.setText(reservationLst.size() + " kết quả");
                        binding.tvNoResults.setVisibility(View.GONE);
                    }

                }, 1500);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReservationHistoryActivity.this, "Lấy thông tin lịch sử đặt phòng thất bại !" + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Nếu muốn xử lý khi bấm nút back trên toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

