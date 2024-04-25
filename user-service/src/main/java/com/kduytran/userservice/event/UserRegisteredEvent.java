package com.kduytran.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString
public class UserRegisteredEvent {
    private String transactionId;
    private String username;
    private String name;
    private String email;
    private String userType;
    private String token;
    private String expiredDate;
}
