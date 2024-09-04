package com.kduytran.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClassRole {
    MEMBER("M");

    private String code;

    public static ClassRole of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(ClassRole.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum ClassRole constant with code " + code));
    }

}
