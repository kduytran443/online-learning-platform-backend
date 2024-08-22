package com.kduytran.paymentservice.payment;

import com.kduytran.paymentservice.entity.TransactionEntity;

public interface InitPaymentStrategy {

    TransactionEntity init();

}
