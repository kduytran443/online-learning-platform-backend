package com.kduytran.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Schema(
        name = "RegistrationDTO",
        description = "Schema to hold registration information"
)
public class RegistrationDTO {

    @NotEmpty(message = "Username can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Username is not valid")
    @Schema(
            description = "Username of user", example = "duytran"
    )
    private String username;

    @NotEmpty(message = "Password can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Password is not valid")
    @Schema(
            description = "Password of user", example = "password123"
    )
    private String password;

    @NotEmpty(message = "Name can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Password is not valid")
    @Schema(
            description = "Name of user", example = "Duy Tran"
    )
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid")
    @Schema(
            description = "Email of user", example = "test@gmail.com"
    )
    private String email;

    @NotEmpty(message = "User type can not be null or empty")
    @Pattern(regexp = "^[A-Z]+$", message = "Type is not valid")
    @Schema(
            description = "Type of User", example = "STUDENT"
    )
    private String userType;

    @NotEmpty(message = "Mobile number can not be null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    @Schema(
            description = "Mobile Number of User", example = "0354437687"
    )
    private String mobilePhone;

}
