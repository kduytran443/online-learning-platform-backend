package com.kduytran.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Schema(
        name = "UserDTO",
        description = "Schema to hold User information"
)
public class UserDTO {
    @Schema(
            description = "Sequence of user", example = "1"
    )
    private long seq;

    @Schema(
            description = "UUID of user", example = "UUID"
    )
    private UUID id;

    @Schema(
            description = "Username of user", example = "duytran"
    )
    private String username;

    @Schema(
            description = "Name of user", example = "Duy Tran"
    )
    private String name;

    @Schema(
            description = "Email of user", example = "test@gmail.com"
    )
    private String email;

    @Schema(
            description = "Type of User", example = "STUDENT"
    )
    private String userType;

    @Schema(
            description = "Status of User", example = "A"
    )
    private String userStatus;

    @Schema(
            description = "Mobile Number of User", example = "0354437687"
    )
    private String mobilePhone;

}
