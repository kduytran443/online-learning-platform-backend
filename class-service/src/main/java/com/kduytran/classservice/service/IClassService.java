package com.kduytran.classservice.service;

import com.kduytran.classservice.dto.*;

import java.util.UUID;

/**
 * This interface defines the contract for operations related to class management.
 */
public interface IClassService {

    /**
     * Creates a new class based on the provided data.
     *
     * @param updateClassDTO The DTO (Data Transfer Object) containing the data needed to create a class.
     */
    UUID create(UpdateClassDTO updateClassDTO);

    /**
     * Updates an existing class with the provided data.
     *
     * @param updateClassDTO The DTO containing the updated data for the class.
     */
    void update(String id, UpdateClassDTO updateClassDTO);

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
    void unHide(String id);

    /**
     * Hides a class, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the class to be hidden.
     */
    void hide(String id);

    /**
     * Checks if the provided password is valid for the specified user or entity.
     *
     * @param id The unique identifier of the user or entity.
     * @param checkPasswordDTO The password to be checked.
     * @return {@code true} if the password is valid, {@code false} otherwise.
     */
    boolean checkPassword(String id, CheckPasswordDTO checkPasswordDTO);


    /**
     * Sets a new password for the specified user or entity.
     *
     * @param id The unique identifier of the user or entity.
     * @param setPasswordDTO The new password to be set.
     */
    void setPassword(String id, SetPasswordDTO setPasswordDTO);

}
