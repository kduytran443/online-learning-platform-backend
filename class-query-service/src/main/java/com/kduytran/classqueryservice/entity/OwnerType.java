package com.kduytran.classqueryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum OwnerType {
    Group("G"),
    User("U");

    private String code;

    public static OwnerType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(OwnerType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum OwnerType constant with code " + code));
    }

}
