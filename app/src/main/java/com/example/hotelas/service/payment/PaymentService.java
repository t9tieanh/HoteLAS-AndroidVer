package com.example.hotelas.service.payment;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.common.PaymentDTO;
import com.example.hotelas.model.common.RequestDTO;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.service.callback.ServiceExecutor;

import retrofit2.Call;

public class PaymentService {
    private final PaymentAPIService apiService;

    public PaymentService(String token) {
        apiService = RetrofitClient.getRetrofitWithAuth(token).create(PaymentAPIService.class);
    }

    public void payVNPay(
            int ammout,
            String reservationId,
            ServiceExecutor.CallBack<PaymentDTO.VNPayResponse> callback
    ) {
        Call<ApiResponse<PaymentDTO.VNPayResponse>> call = apiService.payVNPay(ammout, reservationId);
        ServiceExecutor.enqueue(call, callback);
    }

    public void payAtHotel(
            RequestDTO request,
            ServiceExecutor.CallBack<CreationResponse> callback
    ) {
        Call<ApiResponse<CreationResponse>> call = apiService.payAtHotel(request);
        ServiceExecutor.enqueue(call, callback);
    }
}
