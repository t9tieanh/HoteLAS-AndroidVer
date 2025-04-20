package com.example.hotelas.service.auth;

import android.util.Log;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.response.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private final AuthAPIService apiService;
    public AuthService() {
        apiService = RetrofitClient.getRetrofitInstance().create(AuthAPIService.class);
    }

    public void checkEmailExists(String email ,CallBack callback) {
        Call<ApiResponse<Boolean>> call = apiService.checkEmailExists(email);
        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    // Interface Callback sửa lại để nhận giá trị Boolean từ API
    public interface CallBack {
        void onSuccess(ApiResponse<Boolean> result);
        void onFailure(String errorMessage);
    }
}
