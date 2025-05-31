package com.kduytran.paymentservice.dto;

import com.kduytran.paymentservice.entity.PaymentMethod;
import lombok.Data;

@Data
public class ExecuteTransactionRequestDTO {
    private String paymentId;
    private String payerId;
    private PaymentMethod paymentMethod;
}
