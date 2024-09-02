package com.kduytran.orderservice.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfo {
    private UUID id;
    private String username;
    private String name;
    private String email;

}
