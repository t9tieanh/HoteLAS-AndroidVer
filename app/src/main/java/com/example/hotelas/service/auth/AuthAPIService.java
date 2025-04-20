package com.example.hotelas.service.auth;

import com.example.hotelas.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthAPIService {
    @GET("auth/email-exists")
    Call<ApiResponse<Boolean>> checkEmailExists(@Query("email") String email);
}
