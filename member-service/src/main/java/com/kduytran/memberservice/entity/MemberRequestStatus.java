package com.kduytran.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRequestStatus {
    PENDING,
    REJECTED,
    ACCEPTED
}
