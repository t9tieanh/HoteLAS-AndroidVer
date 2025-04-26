package com.example.hotelas.model.request.reservation.history;

import com.example.hotelas.model.common.AddressDTO;
import  com.example.hotelas.model.request.service.HotelFacilityRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationHistoryRequest {
    String reservationId;
    String hotelName;
    AddressDTO address;
    List<HotelFacilityRequest> facility;
    String roomTypeName;
    double totalPrice;
    String reservationDate;
    String checkInDate;
    String checkOutDate;
}
