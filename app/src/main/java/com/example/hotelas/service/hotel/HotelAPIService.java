package com.example.hotelas.service.hotel;

import androidx.annotation.NonNull;

import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.hotel.HotelDestailResponse;
import com.example.hotelas.model.response.hotel.HotelImageResponse;
import com.example.hotelas.model.response.hotel.HotelImageTypeCountReponse;
import com.example.hotelas.model.response.hotel.HotelResultResponse;
import com.example.hotelas.model.response.paging.PagingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("hotel/detail/{hotelId}")
    Call<ApiResponse<HotelDestailResponse>> getHotelDetail(
            @Path("hotelId") String hotelId,
            @Query("adults") String adults,
            @Query("rooms") String rooms,
            @Query("checkIn")@NonNull  String checkIn,
            @Query("checkOut")@NonNull String checkOut
    );


    @GET("hotel/image-category/{hotelId}")
    Call<ApiResponse<List<HotelImageTypeCountReponse>>> getImageCategory(@Path("hotelId") String hotelId);

    @GET("hotel/image-category")
    Call<ApiResponse<HotelImageResponse>> getImages(
            @Query("hotelId") String hotelId,
            @Query("imageType") String imageType
    );
}
