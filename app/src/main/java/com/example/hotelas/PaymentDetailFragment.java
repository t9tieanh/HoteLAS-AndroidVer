package com.example.hotelas;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import com.example.hotelas.config.PaymentPrefManager;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.FragmentPaymentDetailBinding;
import com.example.hotelas.model.common.PaymentDTO;
import com.example.hotelas.model.common.RequestDTO;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.service.payment.PaymentService;

import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
public class PaymentDetailFragment extends Fragment {
    private FragmentPaymentDetailBinding binding;
    private PaymentService paymentService;
    private PaymentPrefManager paymentPrefManager;

    public PaymentDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get token
        String token = new PrefManager(requireContext()).getAuthResponse().getAccessToken();

        paymentService = new PaymentService(token);
        paymentPrefManager = new PaymentPrefManager(requireContext());

        // Xử lý sự kiện chọn checkbox ví điện tử
        binding.checkboxEWallet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxOnArrival.setChecked(false);

                View parent = (View) binding.tvEWalletDesc.getParent();
                if (parent instanceof LinearLayout) {
                    parent.setVisibility(View.VISIBLE);
                }
                binding.llOnArrivalDesc.setVisibility(View.GONE);
            } else {
                View parent = (View) binding.tvEWalletDesc.getParent();
                if (parent instanceof LinearLayout) {
                    parent.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý sự kiện chọn checkbox thanh toán khi nhận phòng
        binding.checkboxOnArrival.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxEWallet.setChecked(false);
                binding.llOnArrivalDesc.setVisibility(View.VISIBLE);

                View parent = (View) binding.tvEWalletDesc.getParent();
                if (parent instanceof LinearLayout) {
                    parent.setVisibility(View.GONE);
                }
            } else {
                binding.llOnArrivalDesc.setVisibility(View.GONE);
            }
        });

        // xử lý sự kiện khi ấn thanh toán bằng vnpay
        binding.vnPaybtn.setOnClickListener(
                e -> payVNPay()
        );

        // xử lý sự kiện khi ấn thanh toán tại khách sạn
        binding.payAtHotelBtn.setOnClickListener(
                e -> payAtHotel()
        );
    }

    private void payVNPay () {
        String reservationId = ((PaymentActivity)requireActivity()).reservationId;

        Double totalPrice = ((PaymentActivity) requireActivity()).reservationStepResponse.getTotalPrice();
        int amount = totalPrice != null ? totalPrice.intValue() : 0;

        paymentService.payVNPay(amount, reservationId, new ServiceExecutor.CallBack<PaymentDTO.VNPayResponse>() {
            @Override
            public void onSuccess(ApiResponse<PaymentDTO.VNPayResponse> result) {
                Log.d("",result.getResult().paymentUrl);
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(result.getResult().paymentUrl));

                // xóa thông tin của transaction
                paymentPrefManager.clearPaymentInfo();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void payAtHotel () {
        if (binding.checkboxPolicy.isChecked()) {
            String reservationId = ((PaymentActivity)requireActivity()).reservationId;

            RequestDTO request = RequestDTO.builder()
                    .id(reservationId)
                    .build();

            paymentService.payAtHotel(request, new ServiceExecutor.CallBack<CreationResponse>() {
                @Override
                public void onSuccess(ApiResponse<CreationResponse> result) {
                    Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    ((PaymentActivity)requireActivity()).getReservationInfo();

                    // xóa thông tin của transaction
                    paymentPrefManager.clearPaymentInfo();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(requireContext(), "Vui lòng chấp nhận điều khoản trước khi thanh toán", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


