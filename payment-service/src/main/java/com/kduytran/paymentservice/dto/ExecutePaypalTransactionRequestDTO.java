package com.kduytran.paymentservice.dto;

import lombok.Data;

@Data
public class ExecutePaypalTransactionRequestDTO {
    private String paymentId;
    private String payerId;
}
