package com.kduytran.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JoinRequestType {
    JOIN_CLASS("C");

    private String code;

    public static JoinRequestType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(JoinRequestType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum JoinRequestStatus constant with code " + code));
    }

}
