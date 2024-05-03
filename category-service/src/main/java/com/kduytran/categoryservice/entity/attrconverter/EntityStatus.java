package com.kduytran.categoryservice.entity.attrconverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum EntityStatus {
    LIVE("L"),
    DELETED("D"),
    HIDDEN("H");

    private String code;

    public static EntityStatus of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(EntityStatus.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum UserType constant with code " + code));
    }

}
