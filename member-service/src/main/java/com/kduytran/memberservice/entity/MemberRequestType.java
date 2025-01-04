package com.kduytran.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRequestType {
    JOIN_CLASS,
    OUT_CLASS
}
