package com.kduytran.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString
public class UserEvent implements Serializable {
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
