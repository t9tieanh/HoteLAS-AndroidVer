package com.example.hotelas.service.hotel;

import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.hotel.HotelResultResponse;
import com.example.hotelas.model.response.paging.PagingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HotelAPIService {
    @GET("hotel/search-hotel")
    Call<ApiResponse<PagingResponse<HotelResultResponse>>> searchHotels(
            @Query("checkIn") String checkIn,        // ISO format: yyyy-MM-dd
            @Query("checkOut") String checkOut,
            @Query("numAdults") Long numAdults,
            @Query("numRooms") Long numRooms,
            @Query("page") int page,
            @Query("size") int size
    );
}
