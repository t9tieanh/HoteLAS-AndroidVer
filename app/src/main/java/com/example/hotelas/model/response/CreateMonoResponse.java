package com.example.hotelas.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder // no usages
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMonoResponse {

    private String partnerCode;
    private String orderId;
    private String requestId;
    private long amount;
    private long responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
}