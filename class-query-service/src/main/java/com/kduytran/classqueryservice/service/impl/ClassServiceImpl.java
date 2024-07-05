package com.kduytran.classqueryservice.service.impl;

import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.dto.PaginationResponseDTO;
import com.kduytran.classqueryservice.dto.SearchRequestDTO;
import com.kduytran.classqueryservice.repository.ClassRepository;
import com.kduytran.classqueryservice.service.IClassService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new class based on the provided DTO.
     *
     * @param dto The DTO containing class information.
     * @return The UUID of the newly created class.
     */
    @Override
    public UUID create(ClassDTO dto) {

        return null;
    }

    @Override
    public void createBulk(ClassDTO[] dtos) {
    }

    /**
     * Updates an existing class based on the provided DTO.
     *
     * @param dto The DTO containing updated class information.
     */
    @Override
    public void update(String id, ClassDTO dto) {
    }

    /**
     * Deletes a class based on the provided DTO.
     *
     * @param id The string class id to be deleted.
     */
    @Override
    public void delete(String id) {
    }



    /**
     * Searches for classes based on the provided search request DTO and returns a paginated response.
     *
     * @param requestDTO The DTO containing search criteria and pagination details.
     * @return A {@link PaginationResponseDTO} containing a list of {@link ClassDTO} objects.
     */
    @Override
    public PaginationResponseDTO<ClassDTO> search(SearchRequestDTO requestDTO) {

        return null;
    }

    @Override
    public List<ClassDTO> getAllLiveStatus() {
        return null;
    }

}
