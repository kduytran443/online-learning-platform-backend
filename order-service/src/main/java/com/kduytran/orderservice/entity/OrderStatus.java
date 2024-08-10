package com.kduytran.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED("C"),
    PAID("P"),
    FAILED("F"),
    REJECTED("R"),
    COMPLETED("S");

    private String code;

    public static OrderStatus of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(OrderStatus.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum OrderStatus constant with code " + code));
    }

}
