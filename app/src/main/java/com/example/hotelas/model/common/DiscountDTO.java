package com.example.hotelas.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountDTO implements Serializable {
    String name;
    String code;
    LocalDate beginDate;
    LocalDate endDate;

    int discountPrecentage;
    int minloyaltyPoints = 0;
    double minBookingAmount;
    double maxDiscountAmount;
    Boolean isPublic;
}
