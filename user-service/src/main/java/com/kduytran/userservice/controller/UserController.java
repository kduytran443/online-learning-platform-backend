package com.kduytran.userservice.controller;

import com.kduytran.userservice.constant.ResponseConstant;
import com.kduytran.userservice.dto.*;
import com.kduytran.userservice.dto.msg.RegistrationMessageDTO;
import com.kduytran.userservice.service.IUserService;
import com.kduytran.userservice.utils.LogUtils;
import com.kduytran.userservice.utils.TransactionIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;
    private final KafkaTemplate<String, RegistrationMessageDTO> kafkaTemplate;
    private final ServiceContactInfoDTO serviceContactInfoDTO;
    private final TransactionIdHolder transactionIdHolder;

    @Operation(
            summary = "Get contact info",
            description = "REST API to get contract info of user microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<ServiceContactInfoDTO> getContactInfo() {
        logger.debug(LogUtils.getLogDebugFormat("get contract info"), transactionIdHolder.getCurrentTransactionId());
        return ResponseEntity.status(HttpStatus.OK).body(serviceContactInfoDTO);
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
    @PostMapping("/refresh-verification")
    public ResponseEntity<ResponseDTO> refreshUserVerification(@PathParam("userId") String userId) {
        userService.refreshUserVerification(userId);
        return ResponseEntity.ok(ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.REFRESH_VERIFICATION_MESSAGE_201));
    }

    @Operation(
            summary = "Get User Details REST API",
            description = "REST API to get User details inside the system"
    )
    @ApiResponse(
            responseCode = ResponseConstant.STATUS_200,
            description = "HTTP Status OK"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable(value = "id") String id) {
        UserDTO userDTO = userService.fetchUser(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/send-message/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable(value = "message") String message) {
        RegistrationMessageDTO messageDTO = new RegistrationMessageDTO();
        messageDTO.setName(message);
        kafkaTemplate.send("topic-here", messageDTO);
        return ResponseEntity.ok("Send: " + message);
    }

}
