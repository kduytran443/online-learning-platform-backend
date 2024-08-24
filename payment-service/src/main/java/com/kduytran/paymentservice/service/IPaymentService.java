package com.kduytran.paymentservice.service;

import com.kduytran.paymentservice.dto.ExecuteTransactionRequestDTO;
import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.dto.PaymentResponseDTO;

public interface IPaymentService {
    PaymentResponseDTO makeTransaction(PaymentRequestDTO dto);
    boolean executeTransaction(ExecuteTransactionRequestDTO dto);
}
