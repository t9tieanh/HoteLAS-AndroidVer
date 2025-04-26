package com.example.hotelas.service.user;

import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserAPIService {
    @GET("profile")
    Call<ApiResponse<CustomerResponseDTO>> getCustomerProfile();
}
