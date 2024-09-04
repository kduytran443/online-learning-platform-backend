package com.kduytran.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JoinRequestStatus {
    PENDING("P"),
    REJECTED("R"),
    ACCEPTED("A");

    private String code;

    public static JoinRequestStatus of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(JoinRequestStatus.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum JoinRequestStatus constant with code " + code));
    }

}
