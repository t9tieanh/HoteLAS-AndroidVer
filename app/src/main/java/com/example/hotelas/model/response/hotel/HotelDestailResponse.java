package com.example.hotelas.model.response.hotel;

import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.response.room.RoomTypeResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelDestailResponse {
    String id;
    String name;
    String subName;
    String description;
    Double originalPrice;

    AddressDTO address;
    List<FacilitiesResponse> facilities;
    List<String> imgs;

    String avatar;
    List<RoomTypeResponse> rooms;
}
