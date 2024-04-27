package com.kduytran.notificationservice.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisteredEvent implements Serializable {
    private String transactionId;
    private String username;
    private String name;
    private String email;
    private String userType;
    private String token;
    private String expiredDate;
    private String createdAt;
}
