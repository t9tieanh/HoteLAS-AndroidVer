package com.example.hotelas.service.reservation;

import com.example.hotelas.model.common.PaymentDTO;
import com.example.hotelas.model.request.reservation.initial.InitialReservationRequest;
import com.example.hotelas.model.request.reservation.updateinfo.UpdateReservationInfoRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.example.hotelas.model.response.reservation.ReservationStepResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReservationAPIService {
    @POST("reservation")
    Call<ApiResponse<InitialReservationResponse>> createReservation(
            @Body InitialReservationRequest request
    );

    @GET("reservation/current-step")
    Call<ApiResponse<ReservationStepResponse>> getCurrentStep(
            @Query("id") String id
    );

    @POST("reservation/update-info")
    Call<ApiResponse<CreationResponse>> updateCustomerInfoReservation(@Body UpdateReservationInfoRequest request);
}
