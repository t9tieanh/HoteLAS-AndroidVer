package com.example.hotelas.service.auth;

import com.example.hotelas.model.request.AuthenticationRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthAPIService {
    @GET("auth/email-exists")
    Call<ApiResponse<Boolean>> checkEmailExists(@Query("email") String email);

    @POST("auth/login")
    Call<ApiResponse<AuthenticationResponse>> login(@Body AuthenticationRequest request);
}
