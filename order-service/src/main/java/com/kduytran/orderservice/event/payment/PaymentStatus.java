package com.kduytran.orderservice.event.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    FAILED("F"),
    PENDING("P"),
    CANCELLED("C"),
    SUCCESSFUL("S");

    private String code;

    public static PaymentStatus of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(PaymentStatus.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum PaymentStatus constant with code " + code));
    }

}
