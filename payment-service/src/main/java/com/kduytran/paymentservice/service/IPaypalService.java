package com.kduytran.paymentservice.service;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.dto.PaypalResponseDTO;

public interface IPaypalService {
    PaypalResponseDTO createPaypalTransaction(PaymentRequestDTO dto);
    boolean executePaypalTransaction(String paymentId, String payerId);
}
