package com.kduytran.categoryservice.converter;

import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.CreateCategoryDTO;
import com.kduytran.categoryservice.entity.CategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryConverter {

    public static CategoryDTO convert(CategoryEntity entity, CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        dto.setCode(entity.getCode());
        dto.setId(entity.getId().toString());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus().getCode());
        if (entity.getParentCategory() != null) {
            dto.setParentCategory(CategoryConverter.convertWithoutSubList(entity.getParentCategory(), new CategoryDTO()));
        }
        if (entity.getSubCategories() != null) {
            List<CategoryDTO> subCategoryDTOs = entity.getSubCategories().stream().map(
                    sub -> CategoryConverter.convertWithoutParent(sub, new CategoryDTO())
            ).collect(Collectors.toList());
            dto.setSubCategories(subCategoryDTOs);
        }
        return dto;
    }

    public static CategoryDTO convertWithoutSubList(CategoryEntity entity, CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        dto.setCode(entity.getCode());
        dto.setId(entity.getId().toString());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus().getCode());
        if (entity.getParentCategory() != null) {
            dto.setParentCategory(CategoryConverter.convertWithoutSubList(entity.getParentCategory(), new CategoryDTO()));
        }
        return dto;
    }

    public static CategoryDTO convertWithoutParent(CategoryEntity entity, CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        dto.setCode(entity.getCode());
        dto.setId(entity.getId().toString());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus().getCode());
        if (entity.getSubCategories() != null) {
            List<CategoryDTO> subCategoryDTOs = entity.getSubCategories().stream().map(
                    sub -> CategoryConverter.convertWithoutParent(sub, new CategoryDTO())
            ).collect(Collectors.toList());
        }
        return dto;
    }

    public static CategoryEntity convert(CategoryDTO dto, CategoryEntity entity) {
        if (entity == null) {
            entity = new CategoryEntity();
        }
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        if (entity.getId() == null) {
            entity.setId(dto.getId() == null ? null : UUID.fromString(dto.getId()));
        }
        // Set parent entity
        return entity;
    }

    public static CategoryEntity convert(CreateCategoryDTO dto, CategoryEntity entity) {
        if (entity == null) {
            entity = new CategoryEntity();
        }
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        // Set parent entity
        return entity;
    }

}
