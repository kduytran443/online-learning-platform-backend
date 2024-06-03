package com.kduytran.classservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetPasswordDTO {

    @NotEmpty(message = "Password can not be null or empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
