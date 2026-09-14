package com.hudsontran.courseservice.dto.category;

import com.hudsontran.courseservice.enums.CategoryVisibility;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        UUID parentId,
        CategoryVisibility visibility,
        boolean active
) {
}
