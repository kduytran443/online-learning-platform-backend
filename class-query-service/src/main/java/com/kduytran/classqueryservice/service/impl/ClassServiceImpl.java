package com.kduytran.classqueryservice.service.impl;

import com.kduytran.classqueryservice.constant.CommonConstants;
import com.kduytran.classqueryservice.converter.ClassConverter;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.dto.PaginationResponseDTO;
import com.kduytran.classqueryservice.dto.SearchRequestDTO;
import com.kduytran.classqueryservice.entity.ClassEntity;
import com.kduytran.classqueryservice.exception.ClassAlreadyExistsException;
import com.kduytran.classqueryservice.exception.ResourceNotFoundException;
import com.kduytran.classqueryservice.processor.CategoryStreamsProcessor;
import com.kduytran.classqueryservice.repository.ClassRepository;
import com.kduytran.classqueryservice.service.IClassService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;
    private final CategoryStreamsProcessor categoryStreamsProcessor;

    /**
     * Creates a new class based on the provided DTO.
     *
     * @param dto The DTO containing class information.
     * @return The UUID of the newly created class.
     */
    @Override
    public UUID create(ClassDTO dto) {
        boolean alreadyExists = classRepository.existsById(UUID.fromString(dto.getId()));
        if (alreadyExists) {
            throw new ClassAlreadyExistsException(dto.getId());
        }

        ClassEntity classEntity = ClassConverter.convert(dto, new ClassEntity());
        classEntity.setId(UUID.fromString(dto.getId()));
        classEntity.setAverageRating(CommonConstants.DEFAULT_AVERAGE_RATING);
        classEntity.setNumberOfReviews(CommonConstants.DEFAULT_NUMBER_OF_REVIEWS);
        classEntity.setWeightedRating(CommonConstants.DEFAULT_WEIGHTED_RATING);

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
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
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
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        classEntity.setStatus("D");
        classRepository.save(classEntity);
    }

    @Override
    public PaginationResponseDTO<ClassDTO> searchByCategory(SearchRequestDTO requestDTO) {
        List<Sort.Order> orders = new ArrayList<>();
        if (requestDTO.getSortBy() != null) {
            orders.add(new Sort.Order(requestDTO.getDirection(), requestDTO.getSortBy()));
        }
        orders.add(Sort.Order.desc("weightedRating"));

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize() > 0 ? requestDTO.getSize() : SearchRequestDTO.DEFAULT_SIZE,
                Sort.by(orders)
        );

        Page<ClassEntity> entityPage = classRepository.
                findByStatusAndEndAtIsAfterAndAverageRatingBetweenAndCategoryIdIn(
                        "L", LocalDateTime.now(),
                        requestDTO.getMinAverageRating(),
                        requestDTO.getMaxAverageRating(),
                        requestDTO.getCategories(),
                        pageable
                );
        return buildResponseDTO(requestDTO, entityPage);
    }

    private PaginationResponseDTO<ClassDTO> buildResponseDTO(SearchRequestDTO requestDTO, Page<ClassEntity> entityPage) {
        PaginationResponseDTO<ClassDTO> responseDTO = new PaginationResponseDTO<>();
        responseDTO.setSize(requestDTO.getSize());
        responseDTO.setPage(entityPage.getNumber() + 1);
        responseDTO.setTotalElements(entityPage.getTotalElements());
        responseDTO.setTotalPages(entityPage.getTotalPages());
        responseDTO.setItems(entityPage.getContent().stream().map(this::convert).collect(Collectors.toList()));
        return responseDTO;
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
        return list.stream().map(entity -> convert(entity)).collect(Collectors.toList());
    }

    private ClassDTO convert(ClassEntity entity) {
        ClassDTO dto = ClassConverter.convert(entity, new ClassDTO());
        CategoryDTO categoryDTO = categoryStreamsProcessor.getStore().get(dto.getCategoryId());
        if (categoryDTO == null) {
            dto.setCategoryCode(CommonConstants.UNKNOWN);
            dto.setCategoryName(CommonConstants.UNKNOWN);
        } else {
            dto.setCategoryCode(categoryDTO.getCode());
            dto.setCategoryName(categoryDTO.getName());
        }
        return dto;
    }
}
