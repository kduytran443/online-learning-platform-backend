package com.kduytran.authmanagementservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum UserType {
    ADMIN,
    USER
}
