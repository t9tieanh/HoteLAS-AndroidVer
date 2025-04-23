package com.example.hotelas.service.hotel;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.request.AuthenticationRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.AuthenticationResponse;
import com.example.hotelas.model.response.hotel.HotelDestailResponse;
import com.example.hotelas.model.response.hotel.HotelImageResponse;
import com.example.hotelas.model.response.hotel.HotelImageTypeCountReponse;
import com.example.hotelas.model.response.hotel.HotelResultResponse;
import com.example.hotelas.model.response.paging.PagingResponse;
import com.example.hotelas.service.auth.AuthAPIService;
import com.example.hotelas.service.auth.AuthService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelService {
    private final HotelAPIService apiService;

    public HotelService() {
        apiService = RetrofitClient.getRetrofitInstance().create(HotelAPIService.class);
    }

    public void getListHotel(
            Date checkIn,
            Date checkOut,
            Long numAdults,
            Long numRooms,
            int page,
            CallBack callback
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Call<ApiResponse<PagingResponse<HotelResultResponse>>> call = apiService.searchHotels(
                formatter.format(checkIn), formatter.format(checkOut), numAdults, numRooms, page, 5
        );

        call.enqueue(new Callback<ApiResponse<PagingResponse<HotelResultResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagingResponse<HotelResultResponse>>> call, Response<ApiResponse<PagingResponse<HotelResultResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body()); // trả về list khách sạn
                } else {
//                    try {
//                        String errorBody = response.errorBody().string();
//                        Gson gson = new Gson();
//                        ApiResponse errorResponse = gson.fromJson(errorBody, ApiResponse.class);
//                        callback.onFailure(errorResponse.getMessage());
//                    } catch (Exception e) {
//                        callback.onFailure("Không đọc được lỗi từ server");
//                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PagingResponse<HotelResultResponse>>> call, Throwable t) {
                callback.onFailure(t.getMessage() != null ? t.getMessage() : "Lỗi không xác định");
            }
        });
    }


    public void getHotelDetail(
            String hotelId,
            Date checkIn,
            Date checkOut,
            Long numAdults,
            Long numRooms,
            CallBack<HotelDestailResponse> callback
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Call<ApiResponse<HotelDestailResponse>> call = apiService.getHotelDetail(
                hotelId,
                String.valueOf(numAdults),
                String.valueOf(numRooms),
                formatter.format(checkIn),
                formatter.format(checkOut)
        );

        call.enqueue(new Callback<ApiResponse<HotelDestailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<HotelDestailResponse>> call, Response<ApiResponse<HotelDestailResponse>> response) {
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
            public void onFailure(Call<ApiResponse<HotelDestailResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage() != null ? t.getMessage() : "Lỗi không xác định");
            }
        });
    }



    public void getImageType(
            String hotelId,
            CallBack<List<HotelImageTypeCountReponse>> callback
    ) {
        Call<ApiResponse<List<HotelImageTypeCountReponse>>> call = apiService.getImageCategory(hotelId);

        call.enqueue(new Callback<ApiResponse<List<HotelImageTypeCountReponse>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<HotelImageTypeCountReponse>>> call, Response<ApiResponse<List<HotelImageTypeCountReponse>>> response) {
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
            public void onFailure(Call<ApiResponse<List<HotelImageTypeCountReponse>>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


    public void getImages(
            String hotelId,
            String imageType,
            CallBack<HotelImageResponse> callback
    ) {
        Call<ApiResponse<HotelImageResponse>> call = apiService.getImages(hotelId,imageType);

        call.enqueue(new Callback<ApiResponse<HotelImageResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<HotelImageResponse>> call, Response<ApiResponse<HotelImageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không nhận được dữ liệu chi tiết khách sạn");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<HotelImageResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }



    public interface CallBack <T> {
        void onSuccess(ApiResponse<T> result);
        void onFailure(String errorMessage);
    }
}
