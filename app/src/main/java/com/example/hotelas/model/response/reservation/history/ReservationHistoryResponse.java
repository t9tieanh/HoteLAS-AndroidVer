package com.example.hotelas.model.response.reservation.history;

import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.response.reservation.common.HotelFacility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationHistoryResponse {
    String hotelName;
    AddressDTO address;
    List<HotelFacility> facility;
    String roomTypeName;
    double totalPrice;
    LocalDate reservationDate;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    String img;
}
