package com.example.hotelas.model.response.reservation.common;

import com.example.hotelas.enums.RoomStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomType {
    String id;
    String name;
    Long price;
    Long maxOccupation;
    Long freeChildren;
    String description;
    List<String> imgRoomUrl;
    RoomStatusEnum status;
    String avatar;
}
