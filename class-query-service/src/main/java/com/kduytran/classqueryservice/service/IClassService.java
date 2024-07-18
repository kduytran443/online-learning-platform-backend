package com.kduytran.classqueryservice.service;

import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.dto.PaginationResponseDTO;
import com.kduytran.classqueryservice.dto.SearchRequestDTO;

import java.util.List;
import java.util.UUID;

/**
 * Interface defining operations for managing classes.
 */
public interface IClassService {

    /**
     * Creates a new class based on the provided DTO.
     *
     * @param dto The DTO containing class information.
     * @return The UUID of the newly created class.
     */
    UUID create(ClassDTO dto);
    void createBulk(ClassDTO[] dtos);

    /**
     * Updates an existing class based on the provided DTO.
     *
     * @param dto The DTO containing updated class information.
     */
    void update(String id, ClassDTO dto);

    /**
     * Deletes a class based on the provided DTO.
     *
     * @param id The string class id to be deleted.
     */
    void delete(String id);

    PaginationResponseDTO<ClassDTO> searchByCategory(SearchRequestDTO requestDTO);

    PaginationResponseDTO<ClassDTO> search(SearchRequestDTO requestDTO);
    List<ClassDTO> getAllLiveStatus();

}
