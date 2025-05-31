package com.kduytran.categoryservice.service;

import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.SaveCategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getCategories();

    CategoryDTO getOneByCode(String code);

    void create(SaveCategoryDTO saveCategoryDTO);

    void update(SaveCategoryDTO saveCategoryDTO);

    void rebound(String id);

    void delete(String id);
}
