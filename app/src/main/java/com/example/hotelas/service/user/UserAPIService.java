package com.example.hotelas.service.user;

import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPIService {
    @GET("profile")
    Call<ApiResponse<CustomerResponseDTO>> getCustomerProfile();

    @Multipart
    @POST("auth/outbound/active-account")
    Call<ApiResponse<Boolean>> activeAccount(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part img // Đối với ảnh, sử dụng MultipartBody.Part
    );
}
