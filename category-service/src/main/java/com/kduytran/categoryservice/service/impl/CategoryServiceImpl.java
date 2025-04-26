package com.kduytran.categoryservice.service.impl;

import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.SaveCategoryDTO;
import com.kduytran.categoryservice.mapper.CategoryMapper;
import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.categoryservice.service.CategoryService;
import com.kduytran.categoryservice.service.CategoryValidationService;
import com.kduytran.olpcommon.exception.ResourceNotFoundException;
import com.kduytran.olpcommon.validation.CommonValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CommonValidatorService commonValidatorService;
    private final CategoryValidationService categoryValidationService;
    private final ApplicationEventPublisher publisher;

    @Override
    public List<CategoryDTO> getCategories() {
        return categoryMapper.mapToDTOs(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO getOneByCode(String code) {
        var entity = categoryRepository.findOneByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("category", "code", code)
        );
        return categoryMapper.mapToDTO(entity);
    }

    @Override
    public void create(SaveCategoryDTO saveCategoryDTO) {
        commonValidatorService.validate(saveCategoryDTO, CategoryValidationService.createValidationRules,
                categoryValidationService);
        var entity = categoryMapper.mapToEntity(saveCategoryDTO);
        categoryRepository.save(entity);
    }

    @Override
    public void update(SaveCategoryDTO saveCategoryDTO) {
        commonValidatorService.validate(saveCategoryDTO, CategoryValidationService.updateValidationRules,
                categoryValidationService);
        var entity = categoryRepository.findOneById(saveCategoryDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", saveCategoryDTO.getId())
        );
        categoryMapper.mapToEntity(saveCategoryDTO, entity);
        categoryRepository.save(entity);
    }

    @Override
    public void rebound(String id) {
        categoryRepository.restoreById(id);
    }

    @Override
    public void delete(String id) {
        var entity = categoryRepository.findOneById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );
        categoryRepository.delete(entity);
    }
}
