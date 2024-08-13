package com.kduytran.paymentservice.dto;

import com.kduytran.paymentservice.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequestDTO {
    private Double total;
    private UUID orderId;
    private String description;
    private String currency;
    private String cancelUrl;
    private String successUrl;
    private PaymentMethod paymentMethod;
}
