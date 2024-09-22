package com.kduytran.authmanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequestDTO {

    @NotEmpty(message = "Username can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Username is not valid")
    @Schema(
            description = "Username of user", example = "duytran"
    )
    private String username;

    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email should be valid")
    @Schema(
            description = "Email of user", example = "duytran@example.com"
    )
    private String email;

    @Schema(
            description = "Phone number of user", example = "0123456789"
    )
    private String phoneNumber;

    @NotEmpty(message = "Password can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Password is not valid")
    @Schema(
            description = "Password of user", example = "password123"
    )
    private String password;

    @NotEmpty(message = "Name can not be null or empty")
    @Pattern(regexp = "^[a-zA-Z ]{3,30}$", message = "Name is not valid")
    @Schema(
            description = "Name of user", example = "Duy Tran"
    )
    private String name;
}
