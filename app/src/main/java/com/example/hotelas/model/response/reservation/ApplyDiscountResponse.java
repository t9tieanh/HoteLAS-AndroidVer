package com.example.hotelas.model.response.reservation;


import com.example.hotelas.model.common.DiscountDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyDiscountResponse {
    boolean isSuccess;
    Set<DiscountDTO> discounts;
}
