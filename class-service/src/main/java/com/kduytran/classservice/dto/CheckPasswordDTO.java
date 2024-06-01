package com.kduytran.classservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CheckPasswordDTO {

    @NotEmpty(message = "Password can not be null or empty")
    private String password;

}
