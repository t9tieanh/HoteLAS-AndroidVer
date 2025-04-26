package com.example.hotelas.service.payment;

import com.example.hotelas.model.common.PaymentDTO;
import com.example.hotelas.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PaymentAPIService {
//    @GET("/api/v1/payment/vn-pay")
//    Call<ApiResponse<PaymentDTO.VNPayResponse>> payVNPay(
//            @Query("amount") int amount,
//            @Query("id") String reservationId
//    );

    @GET("/api/v1/payment/vn-pay")
    Call<ApiResponse<PaymentDTO.VNPayResponse>> payVNPay(
            @Query("amount") int amount,
            @Query("id") String reservationId,
            @Query("platform") String platform 
    );


    default Call<ApiResponse<PaymentDTO.VNPayResponse>> payVNPay(int amount, String reservationId) {
        return payVNPay(amount, reservationId, "android");
    }
}
