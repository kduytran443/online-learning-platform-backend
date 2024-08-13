package com.kduytran.paymentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    PAYPAL("P");

    private String code;

    public static PaymentMethod of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(PaymentMethod.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum PaymentMethod constant with code " + code));
    }

}
