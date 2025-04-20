package com.example.hotelas.model.response.payment;

import com.example.hotelas.enums.PaymentMethodEnum;
import com.example.hotelas.enums.PaymentStatusEnum;
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
public class PaymentResponse {
    Double amount;
    LocalDate paymentDate;
    PaymentStatusEnum status;
    PaymentMethodEnum paymentMethod;
}