package com.kduytran.classqueryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "ClassDTO represents a class with its associated metadata.")
public class ClassDTO {

    @Schema(description = "Unique identifier for the class.", example = "1234")
    private String id;

    @NotNull(message = "Class name cannot be null.")
    @Schema(description = "Name of the class.", example = "Mathematics 101", required = true)
    private String name;

    @Schema(
            description = "URL or path to the image representing the class.",
            example = "https://example.com/images/class.jpg"
    )
    private String image;

    @Schema(description = "Date and time when the class starts.", example = "2024-07-01T10:00:00")
    private LocalDateTime startAt;

    @Schema(description = "Date and time when the class ends.", example = "2024-07-01T12:00:00")
    private LocalDateTime endAt;

    private LocalDateTime createAt;

    @Pattern(regexp = "^(L|D|H)$", message = "Status must be 'L', 'D', or 'H'.")
    @Schema(description = "Status of the class. 'L' for Live, 'D' for Draft, 'H' for Hidden.", example = "L")
    private String status;

    @NotNull(message = "Category ID cannot be null.")
    @Schema(description = "Unique identifier for the category the class belongs to.", example = "5678", required = true)
    private String categoryId;

    @NotNull(message = "Category code cannot be null.")
    @Schema(description = "Code representing the category the class belongs to.", example = "MATH", required = true)
    private String categoryCode;

    @NotNull(message = "Category name cannot be null.")
    @Schema(description = "Name of the category the class belongs to.", example = "Mathematics", required = true)
    private String categoryName;

    private List<CategoryDTO> categories;

    @Pattern(regexp = "^(G|U)$", message = "Owner type must be 'G' (Group) or 'U' (User).")
    @Schema(description = "Type of the class owner. 'G' for Group, 'U' for User.", example = "U")
    private String ownerType;

    @NotNull(message = "Owner ID cannot be null.")
    @Schema(description = "Unique identifier for the owner of the class.", example = "9012", required = true)
    private String ownerId;

    @NotNull(message = "Owner name cannot be null.")
    @Schema(description = "Name of the owner of the class.", example = "John Doe", required = true)
    private String ownerName;

    @NotNull(message = "Has password field cannot be null.")
    @Schema(
            description = "Indicates whether the class has a password. true if it has a password, false otherwise.",
            example = "true",
            required = true
    )
    private boolean hasPassword;

    private Double averageRating;

    private Long numberOfReviews;

}
