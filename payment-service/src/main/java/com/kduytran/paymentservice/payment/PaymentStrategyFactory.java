package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.repository.TransactionRepository;
import com.paypal.base.rest.APIContext;

import java.util.function.Supplier;

public class PaymentStrategyFactory {

    public static InitPaymentStrategy getInitPaymentStrategy(Supplier<APIContext> apiContext,
                                                             TransactionRepository transactionRepository,
                                                             PaymentRequestDTO dto) {
        return switch (dto.getPaymentMethod()) {
            case PAYPAL -> new PaypalStrategy(apiContext, transactionRepository, dto);
        };
    }

}
