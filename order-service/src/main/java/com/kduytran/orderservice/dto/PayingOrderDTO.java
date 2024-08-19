package com.kduytran.orderservice.dto;

import lombok.Data;

@Data
public class PayingOrderDTO {
    private String orderId;
    private String paymentId;
    private String paymentUrl;
}
