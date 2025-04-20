package com.example.hotelas.model.response.room;

import com.example.hotelas.model.response.reservation.common.RoomType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomAvailabilityResponse {
    RoomType roomType;
    Long totalRoom;
    Long bookedRoom;
    LocalDate availableDate;
    Boolean status;
}
