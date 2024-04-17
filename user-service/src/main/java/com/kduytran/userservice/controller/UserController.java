package com.kduytran.userservice.controller;

import com.kduytran.userservice.constant.ResponseConstant;
import com.kduytran.userservice.dto.ErrorResponseDTO;
import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.ResponseDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "CRUD REST APIs for user microservice"
)
@RequestMapping(
        path = "/api/v1/users",
        produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create User REST API",
            description = "REST API to create new User inside the system and Auth Server"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_201,
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_417,
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        userService.createUser(registrationDTO);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201));
    }

    @Operation(
            summary = "Get User Details REST API",
            description = "REST API to get User details inside the system"
    )
    @ApiResponse(
            responseCode = ResponseConstant.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable(value = "username") String username) {
        UserDTO userDTO = userService.fetchUser(username);
        return ResponseEntity.ok(userDTO);
    }

}
