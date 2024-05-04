package com.kduytran.categoryservice.service.impl;

import com.kduytran.categoryservice.converter.CategoryConverter;
import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.CreateCategoryDTO;
import com.kduytran.categoryservice.entity.CategoryEntity;
import com.kduytran.categoryservice.entity.EntityStatus;
import com.kduytran.categoryservice.exception.CategoryAlreadyExistsException;
import com.kduytran.categoryservice.exception.CategoryNotFoundException;
import com.kduytran.categoryservice.exception.TooManyCategoryParentsException;
import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.categoryservice.service.ICategoryService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    private final int MAX_PARENT_COUNT;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.MAX_PARENT_COUNT = Integer.parseInt("4");
    }

    /**
     * Fetches a category by its unique identifier.
     *
     * @param id The unique identifier for the category.
     * @return A CategoryDTO object representing the category, or null if no category with the given id is found.
     */
    @Override
    public CategoryDTO getOneById(@NonNull String id) {
        CategoryEntity categoryEntity = getLiveEntityById(id);
        return CategoryConverter.convert(categoryEntity, new CategoryDTO());
    }

    /**
     * Fetches a category by its unique code.
     *
     * @param code The unique code for the category.
     * @return A CategoryDTO object representing the category, or null if no category with the given code is found.
     */
    @Override
    public CategoryDTO getOneByCode(@NonNull String code) {
        CategoryEntity categoryEntity = categoryRepository.findByCodeAndStatus(code, EntityStatus.LIVE).orElseThrow(
                () -> new CategoryNotFoundException("Category were not found")
        );
        return CategoryConverter.convert(categoryEntity, new CategoryDTO());
    }

    /**
     * Creates a new category with the provided information.
     *
     * @param createCategoryDTO A CreateCategoryDTO object containing the necessary information to create a new category.
     *                          This includes attributes like name, description, and any other required fields.
     */
    @Override
    @Transactional
    public void create(@NonNull CreateCategoryDTO createCategoryDTO) {
        boolean existsByIdOrCode = categoryRepository.existsByIdOrCode(null, createCategoryDTO.getCode());
        if (existsByIdOrCode) {
            throw new CategoryAlreadyExistsException("Category is already existed");
        }

        CategoryEntity categoryEntity = CategoryConverter.convert(createCategoryDTO, new CategoryEntity());

        // Has parent category
        if (createCategoryDTO.getParentCategoryId() != null) {
            CategoryEntity parentCategoryEntity = getLiveEntityById(createCategoryDTO.getParentCategoryId());
            if (parentCategoryEntity.getParentCount() > MAX_PARENT_COUNT) {
                throw new TooManyCategoryParentsException("Maximum parent category limit exceeded.");
            }
            categoryEntity.setParentCategory(parentCategoryEntity);
        }
        categoryEntity.setStatus(EntityStatus.LIVE);

        logger.debug("Creating category name: {}", categoryEntity.getName());
        categoryRepository.save(categoryEntity);
    }

    /**
     * Updates an existing category with new information.
     *
     * @param id                The unique identifier for the category to be updated.
     * @param createCategoryDTO A CreateCategoryDTO object containing the updated information for the category.
     *                          This includes attributes like name, description, and any other modifiable fields.
     */
    @Override
    @Transactional
    public void update(String id, CreateCategoryDTO createCategoryDTO) {
        CategoryEntity categoryEntity = getLiveEntityById(id);
        CategoryConverter.convert(createCategoryDTO, categoryEntity);

        // Has parent category
        if (createCategoryDTO.getParentCategoryId() != null) {
            CategoryEntity parentCategoryEntity = getLiveEntityById(createCategoryDTO.getParentCategoryId());
            if (parentCategoryEntity.getParentCount() > MAX_PARENT_COUNT) {
                throw new TooManyCategoryParentsException("Maximum parent category limit exceeded.");
            }
            categoryEntity.setParentCategory(parentCategoryEntity);
        }

        logger.debug("Updating category name: {}", categoryEntity.getName());
        categoryRepository.save(categoryEntity);
    }

    /**
     * Restores a previously deleted or hidden category.
     *
     * @param id The unique identifier for the category to be restored.
     */
    @Override
    @Transactional
    public void rebound(String id) {
        CategoryEntity categoryEntity = getLiveEntityById(id);

        List<CategoryEntity> allCategories = new ArrayList<>();
        addAllSubCategoriesToList(allCategories, categoryEntity);

        allCategories = allCategories.stream().map(item -> {
            item.setStatus(EntityStatus.LIVE);
            return item;
        }).collect(Collectors.toList());

        logger.debug("Deleting category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    /**
     * Permanently deletes a category by its unique identifier.
     *
     * @param id The unique identifier for the category to be deleted.
     */
    @Override
    @Transactional
    public void delete(String id) {
        CategoryEntity categoryEntity = getLiveEntityById(id);

        List<CategoryEntity> allCategories = new ArrayList<>();
        addAllSubCategoriesToList(allCategories, categoryEntity);

        allCategories = allCategories.stream().map(item -> {
            item.setStatus(EntityStatus.DELETED);
            return item;
        }).collect(Collectors.toList());

        logger.debug("Deleting category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    /**
     * Hides a category, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the category to be hidden.
     */
    @Override
    @Transactional
    public void hidden(String id) {
        CategoryEntity categoryEntity = getLiveEntityById(id);

        List<CategoryEntity> allCategories = new ArrayList<>();
        addAllSubCategoriesToList(allCategories, categoryEntity);

        allCategories = allCategories.stream().map(item -> {
            item.setStatus(EntityStatus.HIDDEN);
            return item;
        }).collect(Collectors.toList());

        logger.debug("Hiding category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    /**
     * Retrieves a live category entity by its unique identifier.
     *
     * @param id The unique identifier for the category, which is a non-null string.
     * @return A CategoryEntity representing the live category with the specified ID.
     * @throws CategoryNotFoundException If no category with the given ID and live status is found.
     */
    private CategoryEntity getLiveEntityById(@NonNull String id) {
        return categoryRepository.findByIdAndStatus(UUID.fromString(id), EntityStatus.LIVE).orElseThrow(
                () -> new CategoryNotFoundException("Category were not found")
        );
    }

    /**
     * Recursively adds a category and all its subcategories to a list.
     *
     * @param list     The list to which the category and its subcategories will be added.
     * @param category The category from which the subcategories are recursively collected.
     */
    private void addAllSubCategoriesToList(List<CategoryEntity> list, CategoryEntity category) {
        if (category == null) {
            return;
        }
        list.add(category);
        for (CategoryEntity subCategory :
                category.getSubCategories()) {
            addAllSubCategoriesToList(list, subCategory);
        }
    }

}