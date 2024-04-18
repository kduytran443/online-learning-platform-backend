package com.kduytran.notificationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(
        name = "Schema to hold email content"
)
public class EmailDTO {

    @Schema(description = "The subject of the email", example = "Important Notification")
    @NotEmpty(message = "Subject must not be empty")
    private String subject;

    @Schema(description = "The email address of the recipient", example = "recipient@example.com")
    @Email(message = "Invalid recipient email format")
    @NotEmpty(message = "Recipient email must not be empty")
    private String recipientEmail;

    @Schema(description = "The email address of the sender", example = "sender@example.com")
    @Email(message = "Invalid sender email format")
    @NotEmpty(message = "Sender email must not be empty")
    private String senderEmail;

}
