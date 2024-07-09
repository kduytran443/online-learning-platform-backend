package com.kduytran.classqueryservice.converter;

import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.event.CategoryEvent;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryConverter {

    public static CategoryDTO convert(CategoryEvent event) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCode(event.getCode());
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setParentCategory(dto.getParentCategory());
        return dto;
    }

}
