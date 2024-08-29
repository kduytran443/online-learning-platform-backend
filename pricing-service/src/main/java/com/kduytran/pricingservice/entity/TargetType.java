package com.kduytran.pricingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TargetType {
    CLASS("CL");

    private String code;

    public static TargetType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(TargetType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No enum %s constant with code %s",
                        TargetType.class.getName(), code)));
    }

}
