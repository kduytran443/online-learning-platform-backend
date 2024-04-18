package com.kduytran.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("A"),
    INACTIVE("I"),
    DELETED("D"),
    BLOCKED("B");

    private String code;

    public static UserStatus of(String code) {
        return Stream.of(UserStatus.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum UserType constant with code " + code));
    }

}
