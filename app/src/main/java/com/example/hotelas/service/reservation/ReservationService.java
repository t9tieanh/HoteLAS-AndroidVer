package com.example.hotelas.service.reservation;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.request.reservation.initial.InitialReservationRequest;
import com.example.hotelas.model.request.reservation.updateinfo.UpdateReservationInfoRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.example.hotelas.model.response.reservation.ReservationStepResponse;
import com.example.hotelas.model.response.reservation.history.ReservationHistoryResponse;
import com.example.hotelas.service.callback.ServiceExecutor;

import java.util.List;

import retrofit2.Call;

public class ReservationService {

    private final ReservationAPIService apiService;

    public ReservationService(String token) {
        apiService = RetrofitClient.getRetrofitWithAuth(token).create(ReservationAPIService.class);
    }

    public void createReservation(
            InitialReservationRequest request,
            ServiceExecutor.CallBack<InitialReservationResponse> callback
    ) {
        Call<ApiResponse<InitialReservationResponse>> call = apiService.createReservation(request);
        ServiceExecutor.enqueue(call, callback);
    }

    public void getCurrentStep(
            String id,
            ServiceExecutor.CallBack<ReservationStepResponse> callback
    ) {
        Call<ApiResponse<ReservationStepResponse>> call = apiService.getCurrentStep(id);
        ServiceExecutor.enqueue(call, callback);
    }

    public void updateCustomerInfoReservation(
            UpdateReservationInfoRequest request,
            ServiceExecutor.CallBack<CreationResponse> callback
    ) {
        Call<ApiResponse<CreationResponse>> call = apiService.updateCustomerInfoReservation(request);
        ServiceExecutor.enqueue(call, callback);
    }


    // lấy lịch sử đặt phòng
    public void getReservationHistory (
            ServiceExecutor.CallBack<List<ReservationHistoryResponse>> callback
    ) {
        Call<ApiResponse<List<ReservationHistoryResponse>>> call = apiService.getReservationHistory();
        ServiceExecutor.enqueue(call, callback);
    }
}
