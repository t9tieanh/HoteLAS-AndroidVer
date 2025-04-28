package com.example.hotelas.service.user;

import com.example.hotelas.model.request.user.CustomerRequestDTO;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Multipart
    @PUT("/update-user-image")
    Call<ApiResponse<CreationResponse>> updateImage(
            @Part MultipartBody.Part file
    );

    @POST("/edit-profile")
    @FormUrlEncoded
    Call<ApiResponse<CustomerResponseDTO>> editCustomerProfile(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email
    );
}
