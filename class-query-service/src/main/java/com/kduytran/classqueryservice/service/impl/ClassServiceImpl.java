package com.kduytran.classqueryservice.service.impl;

import com.kduytran.classqueryservice.converter.ClassConverter;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.dto.PaginationResponseDTO;
import com.kduytran.classqueryservice.dto.SearchRequestDTO;
import com.kduytran.classqueryservice.entity.ClassEntity;
import com.kduytran.classqueryservice.exception.ResourceNotFoundException;
import com.kduytran.classqueryservice.processor.CategoryStreamProcessor;
import com.kduytran.classqueryservice.repository.ClassRepository;
import com.kduytran.classqueryservice.service.IClassService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;
    private final CategoryStreamProcessor categoryStreamProcessor;

    /**
     * Creates a new class based on the provided DTO.
     *
     * @param dto The DTO containing class information.
     * @return The UUID of the newly created class.
     */
    @Override
    public UUID create(ClassDTO dto) {
        ClassEntity classEntity = ClassConverter.convert(dto, new ClassEntity());
        classEntity.setId(UUID.fromString(dto.getId()));
        classEntity.setAverageRating(0d);
        classEntity.setNumberOfReviews(0L);

        // TODO: Throw error when already existed

        return classRepository.save(classEntity).getId();
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
        ClassEntity classEntity = classRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        classRepository.save(ClassConverter.convert(dto, classEntity));
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
        List<ClassEntity> list = classRepository.findAllByStatusAndEndAtIsAfter("L", LocalDateTime.now());
        Map<String, CategoryDTO> categoryCacheMap = new HashMap<>();

        return list.stream().map(
                entity -> convert(entity, categoryCacheMap)
        ).collect(Collectors.toList());
    }

    private ClassDTO convert(ClassEntity entity, Map<String, CategoryDTO> cacheMap) {
        ClassDTO dto = ClassConverter.convert(entity, new ClassDTO());

        CategoryDTO categoryDTO = cacheMap.computeIfAbsent(
                dto.getCategoryId(), id -> categoryStreamProcessor.findOneById(id)
        );
        if (categoryDTO == null) {
            dto.setCategoryCode("<< UNKNOWN >>");
            dto.setCategoryName("<< UNKNOWN >>");
        } else {
            dto.setCategoryCode(categoryDTO.getCode());
            dto.setCategoryName(categoryDTO.getName());
        }
        return dto;
    }
}
