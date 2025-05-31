package com.kduytran.authmanagementservice.dto;

import com.kduytran.authmanagementservice.entity.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RegistrationDTO {

    @NotEmpty(message = "Username can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Username is not valid")
    private String username;

    @NotEmpty(message = "Password can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Password is not valid")
    private String password;

    @NotEmpty(message = "Name of user can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Name of user is not valid")
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid")
    private String email;

    @NotEmpty(message = "User type can not be null or empty")
    @Pattern(regexp = "^[A-Z]+$", message = "Type is not valid")
    private UserType userType;

    @NotEmpty(message = "Mobile number can not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digits")
    private String mobilePhone;
}
