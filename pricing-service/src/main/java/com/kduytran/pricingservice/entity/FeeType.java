package com.kduytran.pricingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum FeeType {
    MONTH("M"),
    YEAR("Y"),
    QUARTER("Q"),
    ALL("A");

    private String code;

    public static FeeType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(FeeType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum FeeType constant with code " + code));
    }

}
