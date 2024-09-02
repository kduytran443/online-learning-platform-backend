package com.kduytran.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum OrderType {
    BUY_CLASS("BL");

    private String code;

    public static OrderType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(OrderType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum OrderType constant with code " + code));
    }

}
