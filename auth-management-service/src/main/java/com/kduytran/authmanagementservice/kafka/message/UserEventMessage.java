package com.kduytran.authmanagementservice.kafka.message;

import lombok.Data;

@Data
public class UserEventMessage {
    private EventType type;
    private String id;
    private String username;
    private String name;
    private String email;
    private String userType;
    private String token;
    private String expiredDate;
    private String createdAt;
}
