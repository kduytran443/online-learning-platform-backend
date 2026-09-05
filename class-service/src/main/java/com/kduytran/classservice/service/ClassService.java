package com.kduytran.classservice.service;

import com.kduytran.classservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * This interface defines the contract for operations related to class management.
 */
public interface ClassService {

    /**
     * Creates a new class based on the provided data.
     */
    UUID create(SaveClassDTO saveClassDTO, MultipartFile thumbnail, MultipartFile banner);

    /**
     * Updates an existing class with the provided data.
     *
     * @param saveClassDTO The DTO containing the updated data for the class.
     */
    void update(UUID id, SaveClassDTO saveClassDTO);

    /**
     * Permanently deletes a class by its unique identifier.
     *
     * @param id The unique identifier for the class to be deleted.
     */
    void delete(UUID id);

    ClassDetailsDTO get(UUID id);
}
