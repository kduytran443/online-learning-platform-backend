package com.kduytran.categoryservice.service;

import com.kduytran.categoryservice.dto.CategoryDTO;
import com.kduytran.categoryservice.dto.CreateCategoryDTO;

/**
 * The ICategoryService interface defines a contract for managing categories
 * in an application. This service allows for creating, updating, fetching, and
 * managing the lifecycle of categories. Categories might represent various
 * classifications, types, or groups in the application.
 */
public interface ICategoryService {

    /**
     * Fetches a category by its unique identifier.
     *
     * @param id The unique identifier for the category.
     * @return A CategoryDTO object representing the category, or null if no category with the given id is found.
     */
    CategoryDTO getOneById(String id);

    /**
     * Fetches a category by its unique code.
     *
     * @param code The unique code for the category.
     * @return A CategoryDTO object representing the category, or null if no category with the given code is found.
     */
    CategoryDTO getOneByCode(String code);

    /**
     * Creates a new category with the provided information.
     *
     * @param createCategoryDTO A CreateCategoryDTO object containing the necessary information to create a new category.
     *                          This includes attributes like name, description, and any other required fields.
     */
    void create(CreateCategoryDTO createCategoryDTO);

    /**
     * Updates an existing category with new information.
     *
     * @param id                The unique identifier for the category to be updated.
     * @param createCategoryDTO A CreateCategoryDTO object containing the updated information for the category.
     *                          This includes attributes like name, description, and any other modifiable fields.
     */
    void update(String id, CreateCategoryDTO createCategoryDTO);

    /**
     * Restores a category and all its parent categories if they were previously deleted or hidden.
     *
     * @param id The unique identifier for the category to be restored.
     */
    void rebound(String id);

    /**
     * Restores all subcategories of a previously deleted or hidden category.
     *
     * @param id The unique identifier for the category whose subcategories are to be restored.
     */
    void reboundAllItsSubCategories(String id);

    /**
     * Permanently deletes a category by its unique identifier.
     *
     * @param id The unique identifier for the category to be deleted.
     */
    void delete(String id);

    /**
     * Hides a category, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the category to be hidden.
     */
    void hide(String id);

}
