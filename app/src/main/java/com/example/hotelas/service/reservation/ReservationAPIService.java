package com.example.hotelas.service.reservation;

import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.common.PaymentDTO;
import com.example.hotelas.model.common.ResponseDTO;
import com.example.hotelas.model.request.reservation.initial.InitialReservationRequest;
import com.example.hotelas.model.request.reservation.updateinfo.UpdateReservationInfoRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.example.hotelas.model.response.reservation.ReservationStepResponse;
import com.example.hotelas.model.response.reservation.history.ReservationHistoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("reservation/history")
    Call<ApiResponse<List<ReservationHistoryResponse>>> getReservationHistory();

    @GET("reservation/applied-discounts/{id}")
    Call<ApiResponse<List<DiscountDTO>>> getAppliedDiscounts(
            @Path("id") String reservationId
    );

    @GET("reservation/reservations-completed-count")
    Call<ApiResponse<Long>> getReservationCompletedCount();

    @DELETE("reservation/{id}")
    Call<ApiResponse<ResponseDTO>> cancelReservation(@Path("id") String reservationId);
}
