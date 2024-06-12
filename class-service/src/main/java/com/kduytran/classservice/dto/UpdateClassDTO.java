package com.kduytran.classservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateClassDTO {

    @Schema(description = "Name of the class")
    @NotBlank(message = "Name can not be null or empty")
    @Pattern(regexp = "^[a-z0-9]+[a-z0-9_]{3,15}$", message = "Name is not valid")
    private String name;

    @Schema(description = "Banner image of the class", example = "https://example.com/images/banner.jpg")
    @NotBlank(message = "Banner image can not be null or empty")
    @Pattern(
            regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\.(jpg|jpeg|png|gif)$",
            message = "Banner image URL is not valid"
    )
    private String image;

    @Schema(description = "Start time of the class")
    @NotEmpty(message = "Start time can not be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Start time must be in the format yyyy-mm-dd")
    private String startAt;

    // Just validate pattern if not null
    @Schema(description = "End time of the class")
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$", message = "End time must be null or in the format yyyy-mm-dd")
    private String endAt;

    @NotBlank(message = "Status can not be null or empty")
    @Pattern(regexp = "^(Active|Inactive|Cancelled)$", message = "Status is not valid")
    private String status;

    @NotBlank(message = "Category ID can not be null or empty")
    private String categoryId;

    @Schema(
            description = "Information about the Class Owner",
            example = "This is the description of the class owner."
    )
    @NotEmpty(message = "Class owner ID cannot be null or empty")
    private String ownerId;

    @Schema(
            description = "Information about the Class Owner type",
            example = "This is the description of the class owner type."
    )
    @NotEmpty(message = "Class owner type cannot be null or empty")
    private String ownerType;

    @Schema(
            description = "Information about the Class Owner",
            example = "This is the name of the class owner."
    )
    @NotEmpty(message = "Class owner name cannot be null or empty")
    @Pattern(
            regexp = "^[a-z0-9 ]+$",
            message = "Class owner name must only contain lowercase letters (a-z), digits (0-9), and spaces"
    )
    private String ownerName;

}
