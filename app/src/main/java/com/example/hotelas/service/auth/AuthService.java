package com.example.hotelas.service.auth;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.request.AuthenticationRequest;
import com.example.hotelas.model.request.reservation.updateinfo.UpdateReservationInfoRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.utils.ImageUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class AuthService {
    private final AuthAPIService apiService;
    public AuthService() {
        apiService = RetrofitClient.getRetrofitInstance().create(AuthAPIService.class);
    }

    public void checkEmailExists(String email ,CallBack<Boolean> callback) {
        Call<ApiResponse<Boolean>> call = apiService.checkEmailExists(email);
        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onFailure(t.toString());
            }
        });
    }

    public void login(AuthenticationRequest request, CallBack<AuthenticationResponse> callback) {
        Call<ApiResponse<AuthenticationResponse>> call = apiService.login(request);
        call.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Lấy lỗi từ body
                        String errorBody = response.errorBody().string();
                        Gson gson = new Gson();
                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(errorResponse.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage() != null ? t.getMessage() : "Lỗi không xác định");
            }
        });
    }


    // dùng để đăng nập bằng google
    public void outBoundAuthentication(
            String code,
            ServiceExecutor.CallBack<AuthenticationResponse> callback
    ) {
        Call<ApiResponse<AuthenticationResponse>> call = apiService.outBoundAuthentication(code);
        ServiceExecutor.enqueue(call, callback);
    }



    // Interface Callback sửa lại để nhận giá trị Boolean từ API
    public interface CallBack <T> {
        void onSuccess(ApiResponse<T> result);
        void onFailure(String errorMessage);
    }
}
