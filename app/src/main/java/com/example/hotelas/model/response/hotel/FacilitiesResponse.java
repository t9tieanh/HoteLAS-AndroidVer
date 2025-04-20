package com.example.hotelas.model.response.hotel;


import com.example.hotelas.enums.HotelFacilityCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilitiesResponse {
    String name;
    HotelFacilityCategory category;
    String categoryName;
}
