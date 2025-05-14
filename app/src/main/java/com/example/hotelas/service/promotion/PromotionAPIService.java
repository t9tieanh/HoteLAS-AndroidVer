package com.example.hotelas.service.promotion;

import com.example.hotelas.model.common.DiscountDTOForDiscountActivity;
import com.example.hotelas.model.request.reservation.initial.ApplyDiscountRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.reservation.ApplyDiscountResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PromotionAPIService {
    @GET("promotion/available")
    Call<ApiResponse<List<DiscountDTOForDiscountActivity>>> getAvailableDiscounts();

    @POST("reservation/apply-discount")
    Call<ApiResponse<ApplyDiscountResponse>> applyDiscounts(@Body ApplyDiscountRequest request);
}
