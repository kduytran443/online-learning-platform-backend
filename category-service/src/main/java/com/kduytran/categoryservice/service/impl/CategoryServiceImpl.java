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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
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
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.LIVE);
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
            CategoryEntity parentCategoryEntity = getEntityByIdAndStatus(createCategoryDTO.getParentCategoryId(),
                    EntityStatus.LIVE);
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
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.LIVE, EntityStatus.HIDDEN);
        CategoryConverter.convert(createCategoryDTO, categoryEntity);

        // Has parent category
        if (createCategoryDTO.getParentCategoryId() != null) {
            CategoryEntity parentCategoryEntity = getEntityByIdAndStatus(createCategoryDTO.getParentCategoryId(),
                    EntityStatus.LIVE);
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
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.DELETED);

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just rebound only deleted categories
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() == EntityStatus.DELETED;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.LIVE)).collect(Collectors.toList());

        logger.debug("Rebounding category name: {}, and its all related categories", categoryEntity.getName());

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
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.LIVE, EntityStatus.HIDDEN);

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just delete if the category is not deleted
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() != EntityStatus.DELETED;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.DELETED))
                                                                .collect(Collectors.toList());
        logger.debug("Deleting category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    /**
     * Unhides an item identified by the given ID.
     *
     * @param id the identifier of the item to unHide; must not be null or empty.
     */
    @Override
    public void unhide(String id) {
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.HIDDEN);

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just delete if the category is not deleted
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() == EntityStatus.HIDDEN;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.HIDDEN))
                                                                .collect(Collectors.toList());
        logger.debug("UnHide category name: {}, and its all related categories", categoryEntity.getName());

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
    public void hide(String id) {
        CategoryEntity categoryEntity = getEntityByIdAndStatus(id, EntityStatus.LIVE);

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just delete if the category is not deleted
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() == EntityStatus.LIVE;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.HIDDEN))
                                                                .collect(Collectors.toList());

        logger.debug("Hiding category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    private CategoryEntity getEntityByIdAndStatus(@NonNull String id, EntityStatus... statuses) {
        return categoryRepository.findByIdAndStatusIn(UUID.fromString(id), List.of(statuses)).orElseThrow(
                () -> new CategoryNotFoundException("Category were not found")
        );
    }

    /**
     * Adds all subcategories from a given category, along with their nested subcategories, to a specified list.
     *
     * @param list            the list of {@link CategoryEntity} objects to which categories will be added.
     * @param category        the {@link CategoryEntity} object representing the root category from which
     *                        subcategories are added.
     * @param statusCondition a {@link Predicate} to filter the categories before adding them to the list.
     *                        <p>
     *                        This method adds the provided category and all its subcategories to the list, provided
     *                        they meet the specified condition.
     *                        If the root category is null, the method returns immediately. The method uses recursion
     *                        to traverse all levels of nested subcategories.
     */
    private void addAllSubCategoriesToList(List<CategoryEntity> list, CategoryEntity category,
                                           Predicate<CategoryEntity> statusCondition) {
        if (category == null) {
            return;
        }
        list.add(category);

        List<CategoryEntity> subCategories =
                category.getSubCategories().stream().filter(statusCondition).collect(Collectors.toList());

        for (CategoryEntity subCategory : subCategories) {
            addAllSubCategoriesToList(list, subCategory, statusCondition);
        }
    }

    private Function<CategoryEntity, CategoryEntity> changeCategoryStatus(EntityStatus status) {
        return category -> {
            category.setStatus(status);
            return category;
        };
    }

}
