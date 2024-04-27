package com.kduytran.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Schema(
        name = "RegistrationDTO",
        description = "Schema to hold registration information sent to notification"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationMessageDTO implements Serializable {
    private String username;
    private String name;
    private String email;
    private String userType;
    private String token;
    private String expiredDate;
    private String createdAt;
}
