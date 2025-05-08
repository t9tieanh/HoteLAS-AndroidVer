package com.example.hotelas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.adapter.DiscountAdapter;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.ActivityVoucherBoxBinding;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.common.DiscountDTOForDiscountActivity;
import com.example.hotelas.model.request.reservation.initial.ApplyDiscountRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.reservation.ApplyDiscountResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.promotion.PromotionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscountActivity extends AppCompatActivity {
    private List<DiscountDTOForDiscountActivity> discounts;
    private PromotionService promotionService;
    private ActivityVoucherBoxBinding binding;
    private DiscountAdapter adapter;
    private PrefManager prefManager;
    private String reservationId = "f9b5905e-10ba-4072-8568-bca3f940f542";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVoucherBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reservationId = getIntent().getStringExtra("reservationId");

        binding.voucherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.btnApplyVoucher.setOnClickListener(v -> applyDiscounts());

        // Thiết lập RecyclerView
        prefManager = new PrefManager(this);
        promotionService = new PromotionService(prefManager.getAuthResponse().getAccessToken());

        getAvailableDiscounts();
    }

    private void markSelectedDiscount () {
        if (getIntent() != null && getIntent().hasExtra("discounts")) {
            ArrayList<DiscountDTO> selectedDiscounts =
                    (ArrayList<DiscountDTO>) getIntent().getSerializableExtra("discounts");

            // Chuyển selectedDiscounts thành Set id để tìm nhanh
            Set<String> selectedCodes = new HashSet<>();
            for (DiscountDTO d : selectedDiscounts) {
                selectedCodes.add(d.getCode());
            }

            discounts.stream().forEach((discountDTO -> {
                discountDTO.setSelected(selectedCodes.contains(discountDTO.getCode()));
            }));
            adapter.notifyDataSetChanged();
        }
    }

    private void getAvailableDiscounts() {
        promotionService.getAvailableDiscounts(new ServiceExecutor.CallBack<List<DiscountDTOForDiscountActivity>>() {
            @Override
            public void onSuccess(ApiResponse<List<DiscountDTOForDiscountActivity>> result) {
                discounts = result.getResult();
                adapter = new DiscountAdapter(DiscountActivity.this, discounts);
                binding.voucherRecyclerView.setAdapter(adapter);

                // đánh dấu lại những discount đã được apply
                markSelectedDiscount();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(DiscountActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyDiscounts () {
        ApplyDiscountRequest request = ApplyDiscountRequest.builder()
                .reservationId(reservationId)
                .discountCodes(adapter.getSelectedDiscounsCode())
                .build();

        promotionService.applyDiscounts(request, new ServiceExecutor.CallBack<ApplyDiscountResponse>() {
            @Override
            public void onSuccess(ApiResponse<ApplyDiscountResponse> result) {
                Toast.makeText(DiscountActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(DiscountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
