package com.kduytran.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@ToString
public enum UserType {
    ADMIN("ADMIN", "Administrator"),
    USER("USER", "Normal User"),
    TEACHER("TEACHER", "Teacher"),
    STUDENT("STUDENT", "Student");

    private String code;
    private String name;

    public static UserType of(String code) {
        if (code == null) {
            throw new RuntimeException();
        }
        return Stream.of(UserType.values())
                .filter(type -> code.equals(type.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum UserType constant with code " + code));
    }

}
