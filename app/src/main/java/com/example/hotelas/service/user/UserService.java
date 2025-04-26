package com.example.hotelas.service.user;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.model.response.reservation.ReservationStepResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.reservation.ReservationAPIService;
import com.example.hotelas.service.reservation.ReservationService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private final UserAPIService apiService;

    public UserService(String token) {
        apiService = RetrofitClient.getRetrofitWithAuth(token).create(UserAPIService.class);
    }

    public void getCustomerProfile(ServiceExecutor.CallBack<CustomerResponseDTO> callback) {
        Call<ApiResponse<CustomerResponseDTO>> call = apiService.getCustomerProfile();
        ServiceExecutor.enqueue(call, callback);
    }
}
