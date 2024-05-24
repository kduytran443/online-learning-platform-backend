package com.kduytran.classservice.service.impl;

import com.kduytran.classservice.converter.ClassConverter;
import com.kduytran.classservice.dto.CreateClassDTO;
import com.kduytran.classservice.dto.PageableDTO;
import com.kduytran.classservice.dto.SimpleClassDTO;
import com.kduytran.classservice.dto.UpdateClassDTO;
import com.kduytran.classservice.entity.ClassEntity;
import com.kduytran.classservice.entity.EntityStatus;
import com.kduytran.classservice.exception.ResourceNotFoundException;
import com.kduytran.classservice.repository.ClassRepository;
import com.kduytran.classservice.service.IClassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;

    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    /**
     * Creates a new class based on the provided data.
     *
     * @param createClassDTO The DTO (Data Transfer Object) containing the data needed to create a class.
     */
    @Override
    public void create(CreateClassDTO createClassDTO) {


    }

    /**
     * Updates an existing class with the provided data.
     *
     * @param updateClassDTO The DTO containing the updated data for the class.
     */
    @Override
    public void update(UpdateClassDTO updateClassDTO) {

    }

    /**
     * Retrieves information about a class based on its unique identifier.
     *
     * @param id The unique identifier of the class to retrieve.
     * @return Information about the class identified by the given ID, or null if no class with the specified ID exists.
     */
    @Override
    public SimpleClassDTO getSimpleById(String id) {

        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Class", "id", id)
        );

        SimpleClassDTO simpleClassDTO = ClassConverter.convert(classEntity, new SimpleClassDTO());

        // TODO - call API to category service
        
        return simpleClassDTO;
    }

    /**
     * Retrieves a paginated list of simple class DTOs for the specified category ID.
     *
     * @param categoryId the ID of the category to fetch classes for
     * @param page
     * @param size
     * @return a {@link PageableDTO} containing {@link SimpleClassDTO} objects and pagination info
     */
    @Override
    public PageableDTO<SimpleClassDTO> getSimpleClassesByCategoryId(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassEntity> classEntityPage = classRepository.findByCategoryIdAndStatus(
                categoryId, EntityStatus.LIVE, pageable
        );
        return ClassConverter.convert(classEntityPage);
    }

    /**
     * Restores a class and all its parent categories if they were previously deleted or hidden.
     *
     * @param id The unique identifier for the class to be restored.
     */
    @Override
    public void rebound(String id) {

    }

    /**
     * Permanently deletes a class by its unique identifier.
     *
     * @param id The unique identifier for the class to be deleted.
     */
    @Override
    public void delete(String id) {

    }

    /**
     * Unhides an item identified by the given ID.
     *
     * @param id the identifier of the item to unhide; must not be null or empty.
     */
    @Override
    public void unhide(String id) {

    }

    /**
     * Hides a class, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the class to be hidden.
     */
    @Override
    public void hide(String id) {

    }

}
