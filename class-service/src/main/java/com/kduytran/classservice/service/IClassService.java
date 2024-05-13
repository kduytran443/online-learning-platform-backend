package com.kduytran.classservice.service;

import com.kduytran.classservice.dto.CreateClassDTO;
import com.kduytran.classservice.dto.SimpleClassDTO;
import com.kduytran.classservice.dto.UpdateClassDTO;

/**
 * This interface defines the contract for operations related to class management.
 */
public interface IClassService {

    /**
     * Creates a new class based on the provided data.
     *
     * @param createClassDTO The DTO (Data Transfer Object) containing the data needed to create a class.
     */
    void create(CreateClassDTO createClassDTO);

    /**
     * Updates an existing class with the provided data.
     *
     * @param updateClassDTO The DTO containing the updated data for the class.
     */
    void update(UpdateClassDTO updateClassDTO);

    /**
     * Retrieves information about a class based on its unique identifier.
     *
     * @param id The unique identifier of the class to retrieve.
     * @return Information about the class identified by the given ID, or null if no class with the specified ID exists.
     */
    SimpleClassDTO getSimpleById(String id);

    /**
     * Restores a class and all its parent categories if they were previously deleted or hidden.
     *
     * @param id The unique identifier for the class to be restored.
     */
    void rebound(String id);

    /**
     * Permanently deletes a class by its unique identifier.
     *
     * @param id The unique identifier for the class to be deleted.
     */
    void delete(String id);

    /**
     * Unhides an item identified by the given ID.
     *
     * @param id the identifier of the item to unhide; must not be null or empty.
     */
    void unhide(String id);

    /**
     * Hides a class, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the class to be hidden.
     */
    void hide(String id);

}
