package com.kduytran.classservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    private UUID id;
    private String name;
    private String accessibility;
    private UUID categoryId;
    private UUID ownerId;
}
