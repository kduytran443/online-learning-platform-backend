package com.kduytran.paymentservice.service;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.dto.PaymentResponseDTO;

public interface IPaypalService {
    PaymentResponseDTO createPaypalTransaction(PaymentRequestDTO dto);
    boolean executePaypalTransaction(String paymentId, String payerId);
    void cancelPaypalTransaction(String paymentId, String payerId);
}
