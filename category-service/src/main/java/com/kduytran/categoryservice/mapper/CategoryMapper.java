package com.kduytran.categoryservice.mapper;

import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.SaveCategoryDTO;
import com.kduytran.categoryservice.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    public abstract CategoryDTO mapToDTO(CategoryEntity categoryEntity);

    public abstract List<CategoryDTO> mapToDTOs(List<CategoryEntity> categoryEntities);

    public abstract CategoryEntity mapToEntity(SaveCategoryDTO saveCategoryDTO);

    public abstract void mapToEntity(SaveCategoryDTO saveCategoryDTO, @MappingTarget CategoryEntity categoryEntity);
}
