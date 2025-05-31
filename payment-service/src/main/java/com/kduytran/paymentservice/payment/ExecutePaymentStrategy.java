package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.dto.ExecuteTransactionRequestDTO;
import com.kduytran.paymentservice.entity.TransactionEntity;
import com.kduytran.paymentservice.exception.PaymentMethodNotSupportedException;
import com.kduytran.paymentservice.repository.TransactionRepository;
import com.paypal.base.rest.APIContext;

import java.util.function.Supplier;

public interface ExecutePaymentStrategy {

    static ExecutePaymentStrategy of(Supplier<APIContext> apiContext,
                                     TransactionRepository transactionRepository,
                                     ExecuteTransactionRequestDTO dto) {
        return switch (dto.getPaymentMethod()) {
            case PAYPAL -> new PaypalStrategy(apiContext, transactionRepository, dto);
            default -> throw new PaymentMethodNotSupportedException(String.format("Payment method %a is not supported.",
                    dto.getPaymentMethod()));
        };
    }

    TransactionEntity execute();

}
