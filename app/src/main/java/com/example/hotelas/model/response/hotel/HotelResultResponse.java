package com.example.hotelas.model.response.hotel;

import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.response.reservation.common.RoomType;
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
public class HotelResultResponse {
    String id;
    String name;
    AddressDTO address;
    int rating;
    List<RoomType> roomType;
    String avatar;
    Double originalPrice;
    double minRoomPrice;
}
