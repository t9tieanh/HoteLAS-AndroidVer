package com.example.hotelas.service.callback;

import com.example.hotelas.model.response.ApiResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceExecutor {
    public static <T> void enqueue(
            Call<ApiResponse<T>> call,
            CallBack<T> callback
    ) {
        call.enqueue(new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(
                    Call<ApiResponse<T>> call,
                    Response<ApiResponse<T>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        ApiResponse<?> err = new Gson()
                                .fromJson(errorBody, ApiResponse.class);
                        callback.onFailure(err.getMessage());
                    } catch (Exception e) {
                        callback.onFailure("Không đọc được lỗi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface CallBack<T> {
        void onSuccess(ApiResponse<T> result);
        void onFailure(String errorMessage);
    }
}