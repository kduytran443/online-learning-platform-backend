package com.kduytran.classservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateClassDescriptionDTO {

    @NotEmpty(message = "Class description can not be null or empty")
    private String content;

}
