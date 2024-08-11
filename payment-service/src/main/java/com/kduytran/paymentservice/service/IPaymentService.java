package com.kduytran.paymentservice.service;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface IPaymentService {
    Payment createPayment(PaymentRequestDTO dto) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
