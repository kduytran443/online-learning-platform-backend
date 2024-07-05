package com.kduytran.userservice.event;

import lombok.Data;

@Data
public class UserEvent {
    private String transactionId;
    private String action;
    private String id;
    private String username;
    private String name;
    private String email;
    private String userType;
    private String token;
    private String expiredDate;
    private String createdAt;
}
