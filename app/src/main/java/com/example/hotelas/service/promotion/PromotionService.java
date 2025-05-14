package com.example.hotelas.service.promotion;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.common.DiscountDTOForDiscountActivity;
import com.example.hotelas.model.request.reservation.initial.ApplyDiscountRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.reservation.ApplyDiscountResponse;
import com.example.hotelas.service.callback.ServiceExecutor;

import java.util.List;

import retrofit2.Call;

public class PromotionService {
    private final PromotionAPIService apiService;

    public PromotionService(String token) {
        apiService = RetrofitClient.getRetrofitWithAuth(token).create(PromotionAPIService.class);
    }

    public void getAvailableDiscounts(
            ServiceExecutor.CallBack<List<DiscountDTOForDiscountActivity>> callback
    ) {
        Call<ApiResponse<List<DiscountDTOForDiscountActivity>>> call = apiService.getAvailableDiscounts();
        ServiceExecutor.enqueue(call, callback);
    }

    public void applyDiscounts (
            ApplyDiscountRequest request,
            ServiceExecutor.CallBack<ApplyDiscountResponse> callBack
    ) {
        Call<ApiResponse<ApplyDiscountResponse>> call = apiService.applyDiscounts(request);
        ServiceExecutor.enqueue(call, callBack);
    }
}
