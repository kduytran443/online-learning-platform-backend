package com.kduytran.categoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
@Data
@AllArgsConstructor(staticName = "of")
public class IdResponseDTO {

    @Schema(description = "Status code in the response", example = "200")
    private String statusCode;

    @Schema(description = "Status message in the response", example = "Request processed successfully")
    private String statusMsg;

    @Schema(description = "Id of the resource", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

}
