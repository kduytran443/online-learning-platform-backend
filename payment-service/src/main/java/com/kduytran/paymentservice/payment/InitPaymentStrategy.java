package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.dto.PaymentRequestDTO;
import com.kduytran.paymentservice.entity.TransactionEntity;
import com.kduytran.paymentservice.repository.TransactionRepository;
import com.paypal.base.rest.APIContext;

import java.util.function.Supplier;

public interface InitPaymentStrategy {

    static InitPaymentStrategy of(Supplier<APIContext> apiContext,
                                  TransactionRepository transactionRepository,
                                  PaymentRequestDTO dto) {
        return switch (dto.getPaymentMethod()) {
            case PAYPAL -> new PaypalStrategy(apiContext, transactionRepository, dto);
            case MASTER_CARD -> new MasterCardStrategy(transactionRepository, dto);
        };
    }

    TransactionEntity init();

}
