package com.kduytran.userservice.controller;

import com.kduytran.userservice.constant.ResponseConstant;
import com.kduytran.userservice.dto.ErrorResponseDTO;
import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.ResponseDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.dto.msg.RegistrationMessageDTO;
import com.kduytran.userservice.event.handler.RabbitMQProducer;
import com.kduytran.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    public UserController(IUserService userService, RabbitMQProducer rabbitMQProducer) {
        this.userService = userService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @Operation(
            summary = "Create User REST API",
            description = "REST API to create new User inside the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_201,
                    description = "HTTP Status CREATED"
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
            summary = "Verify User REST API",
            description = "REST API to verify User inside the system and Auth Server"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_201,
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/verification")
    public ResponseEntity<ResponseDTO> verifyUser(@PathParam("token") String token) {
        userService.verifyUserRegistration(token);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201));
    }

    @Operation(
            summary = "Verify User REST API",
            description = "REST API to refresh User verification inside the system and Auth Server"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_201,
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = ResponseConstant.STATUS_500,
                    description = "HTTP Status INTERNAL SERVER ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/refresh-verification")
    public ResponseEntity<ResponseDTO> refreshUserVerification(@PathParam("userId") String userId) {
        userService.refreshUserVerification(userId);
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
        RegistrationMessageDTO messageDTO = new RegistrationMessageDTO();

        messageDTO.setUsername(username);
        messageDTO.setUserType("STUDENT");
        messageDTO.setName("Example Name");
        messageDTO.setToken(UUID.randomUUID().toString());
        messageDTO.setEmail("trankhanhduy18@gmail.com");
        messageDTO.setExpiredDate(LocalDateTime.now().plusDays(1).toString());

        rabbitMQProducer.sendMessage(messageDTO);
        UserDTO userDTO = userService.fetchUser(username);
        return ResponseEntity.ok(userDTO);
    }

}
