package com.kduytran.categoryservice.service.impl;

import com.kduytran.categoryservice.converter.CategoryConverter;
import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.CreateCategoryDTO;
import com.kduytran.categoryservice.entity.CategoryEntity;
import com.kduytran.categoryservice.entity.EntityStatus;
import com.kduytran.categoryservice.event.CategoryCreatedEvent;
import com.kduytran.categoryservice.event.CategoryDeletedEvent;
import com.kduytran.categoryservice.event.CategoryUpdatedEvent;
import com.kduytran.categoryservice.exception.CategoryAlreadyExistsException;
import com.kduytran.categoryservice.exception.CategoryNotFoundException;
import com.kduytran.categoryservice.exception.CategoryNotInStatusException;
import com.kduytran.categoryservice.exception.TooManyCategoryParentsException;
import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.categoryservice.service.ICategoryService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher publisher;

    private final int MAX_PARENT_COUNT;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ApplicationEventPublisher publisher) {
        this.categoryRepository = categoryRepository;
        this.publisher = publisher;
        this.MAX_PARENT_COUNT = Integer.parseInt("2");
    }

    /**
     * Retrieves a list of root categories.
     * <p>
     * Root categories are the top-level categories in the category hierarchy,
     * having no parent category. This method returns all such root categories.
     *
     * @return A list of CategoryDTO objects representing the root categories.
     */
    @Override
    @Cacheable(cacheNames = "root-categories")
    public List<CategoryDTO> getRootCategoryList() {
        List<CategoryEntity> categoryEntities =
                categoryRepository.findAllByParentCategory(null).orElseGet(ArrayList::new);
        return CategoryConverter.convertWithoutSubList(categoryEntities);
    }

    /**
     * Fetches a category by its unique identifier.
     *
     * @param id The unique identifier for the category.
     * @return A CategoryDTO object representing the category, or null if no category with the given id is found.
     */
    @Override
    @Cacheable(cacheNames = "category", key = "#id")
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
    @Cacheable(cacheNames = "category", key = "#code")
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
     * @return A UUID representing the unique identifier of the newly created category.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true)
            }
    )
    public UUID create(@NonNull CreateCategoryDTO createCategoryDTO) {
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

        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        publishCreatedUser(savedEntity);

        return savedEntity.getId();
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
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true),
                    @CacheEvict(cacheNames = "category", key = "#id"),
                    @CacheEvict(cacheNames = "category", key = "#createCategoryDTO.code")
            }
    )
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
        categoryEntity = categoryRepository.save(categoryEntity);
        publishUpdatedUser(categoryEntity);
    }

    /**
     * Restores a previously deleted or hidden category.
     *
     * @param id The unique identifier for the category to be restored.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true),
                    @CacheEvict(cacheNames = "category", allEntries = true)
            }
    )
    public void rebound(String id) {
        CategoryEntity categoryEntity = getEntityById(id);

        if (categoryEntity.getStatus() != EntityStatus.DELETED) {
            throw new CategoryNotInStatusException("Cannot rebound: Category with ID "
                                                            + id + " is not in DELETE status.");
        }
        List<CategoryEntity> allCategories = categoryEntity.getAllParents();

        // add current entity
        allCategories.add(categoryEntity);

        allCategories = allCategories.stream().filter(entity -> entity.getStatus() == EntityStatus.DELETED)
                .map(changeCategoryStatus(EntityStatus.LIVE))
                .collect(Collectors.toList());
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
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true),
                    @CacheEvict(cacheNames = "category", allEntries = true)
            }
    )
    public void delete(String id) {
        CategoryEntity categoryEntity = getEntityById(id);

        if (categoryEntity.getStatus() != EntityStatus.LIVE) {
            throw new CategoryNotInStatusException("Cannot delete: Category with ID "
                    + id + " is not in LIVE or Hidden status.");
        }

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just delete if the category is not deleted
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() != EntityStatus.DELETED;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.DELETED))
                                                                .collect(Collectors.toList());
        logger.debug("Deleting category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
        publishDeletedUsers(allCategories);
    }

    /**
     * Unhides an item identified by the given ID.
     *
     * @param id the identifier of the item to unHide; must not be null or empty.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true),
                    @CacheEvict(cacheNames = "category", allEntries = true)
            }
    )
    public void unhide(String id) {
        CategoryEntity categoryEntity = getEntityById(id);

        if (categoryEntity.getStatus() != EntityStatus.HIDDEN) {
            throw new CategoryNotInStatusException("Cannot un hide: Category with ID "
                    + id + " is not in Hidden status.");
        }
        List<CategoryEntity> allCategories = categoryEntity.getAllParents();

        // add current entity
        allCategories.add(categoryEntity);

        allCategories = allCategories.stream().filter(entity -> entity.getStatus() == EntityStatus.HIDDEN)
                                                                .map(changeCategoryStatus(EntityStatus.LIVE))
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
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "root-categories", allEntries = true),
                    @CacheEvict(cacheNames = "category", allEntries = true)
            }
    )
    public void hide(String id) {
        CategoryEntity categoryEntity = getEntityById(id);

        if (categoryEntity.getStatus() != EntityStatus.LIVE) {
            throw new CategoryNotInStatusException("Cannot hide: Category with ID "
                    + id + " is not in Live status.");
        }

        List<CategoryEntity> allCategories = new ArrayList<>();

        // Just delete if the category is not deleted
        Predicate<CategoryEntity> statusCondition = entity -> entity.getStatus() == EntityStatus.LIVE;

        addAllSubCategoriesToList(allCategories, categoryEntity, statusCondition);

        allCategories = allCategories.stream().map(changeCategoryStatus(EntityStatus.HIDDEN))
                                                                .collect(Collectors.toList());

        logger.debug("Hiding category name: {}, and its all related categories", categoryEntity.getName());

        categoryRepository.saveAll(allCategories);
    }

    private CategoryEntity getEntityById(@NonNull String id) {
        return categoryRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new CategoryNotFoundException("Category were not found")
        );
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

    private void publishCreatedUser(CategoryEntity categoryEntity) {
        CategoryCreatedEvent event = new CategoryCreatedEvent();
        event.setId(categoryEntity.getId().toString());
        event.setName(categoryEntity.getName());
        event.setDescription(categoryEntity.getDescription());
        event.setCode(categoryEntity.getCode());
        CategoryEntity parent = categoryEntity.getParentCategory();
        if (parent != null) {
            event.setParentCategoryId(parent.getId().toString());
        }
        publisher.publishEvent(event);
    }

    private void publishUpdatedUser(CategoryEntity categoryEntity) {
        CategoryUpdatedEvent event = new CategoryUpdatedEvent();
        event.setId(categoryEntity.getId().toString());
        event.setName(categoryEntity.getName());
        event.setDescription(categoryEntity.getDescription());
        event.setCode(categoryEntity.getCode());
        CategoryEntity parent = categoryEntity.getParentCategory();
        if (parent != null) {
            event.setParentCategoryId(parent.getId().toString());
        }
        publisher.publishEvent(event);
    }

    private void publishDeletedUsers(List<CategoryEntity> categoryEntities) {
        for (CategoryEntity categoryEntity : categoryEntities) {
            CategoryDeletedEvent event = new CategoryDeletedEvent();
            event.setId(categoryEntity.getId().toString());
            event.setName(categoryEntity.getName());
            event.setDescription(categoryEntity.getDescription());
            event.setCode(categoryEntity.getCode());
            CategoryEntity parent = categoryEntity.getParentCategory();
            if (parent != null) {
                event.setParentCategoryId(parent.getId().toString());
            }
            publisher.publishEvent(event);
        }
    }

}
