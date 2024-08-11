package com.kduytran.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequestDTO {
    private Double total;
    private String currency;
    private PaypalPaymentMethod method;
    private PaypalPaymentIntent intent;
    private String cancelUrl;
    private String successUrl;
}
