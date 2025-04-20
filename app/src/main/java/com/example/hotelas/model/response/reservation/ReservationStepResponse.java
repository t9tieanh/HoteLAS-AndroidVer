package com.example.hotelas.model.response.reservation;

import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.response.reservation.common.ReservationDetailResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationStepResponse {
    int currentStep;
    LocalDateTime expireDateTime;
    String description;
    Double totalPrice;

    // ngày check in check out
    LocalDate checkIn;
    LocalDate checkOut;

    // thông tin phòng đặt
    List<ReservationDetailResponse> reservationDetail;

    //Thông tin discount được áp dụng
    Set<DiscountDTO> discounts;
}
