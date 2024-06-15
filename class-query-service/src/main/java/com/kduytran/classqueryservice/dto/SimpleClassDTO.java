package com.kduytran.classqueryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleClassDTO {
    private String id;
    private String name;
    private String image;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private CategoryDTO category;
    private String ownerType;
    private String ownerId;
    private String ownerName;
}
