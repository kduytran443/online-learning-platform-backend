package com.kduytran.classservice.service.impl;

import com.kduytran.classservice.converter.ClassConverter;
import com.kduytran.classservice.dto.*;
import com.kduytran.classservice.entity.ClassEntity;
import com.kduytran.classservice.entity.EntityStatus;
import com.kduytran.classservice.event.Action;
import com.kduytran.classservice.event.application.AbstractClassApplicationEvent;
import com.kduytran.classservice.event.application.ClassCreatedApplicationEvent;
import com.kduytran.classservice.event.application.ClassDeletedApplicationEvent;
import com.kduytran.classservice.event.application.ClassUpdatedApplicationEvent;
import com.kduytran.classservice.exception.EntityStatusNotValidException;
import com.kduytran.classservice.exception.ResourceNotFoundException;
import com.kduytran.classservice.repository.ClassRepository;
import com.kduytran.classservice.service.IClassService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements IClassService {

    private final ClassRepository classRepository;
    private final ApplicationEventPublisher publisher;

    /**
     * Creates a new class based on the provided data.
     *
     * @param updateClassDTO The DTO (Data Transfer Object) containing the data needed to create a class.
     */
    @Override
    @Transactional
    public UUID create(UpdateClassDTO updateClassDTO) {
        ClassEntity classEntity = ClassConverter.convert(updateClassDTO, new ClassEntity());
        classEntity = classRepository.save(classEntity);
        pushEvent(classEntity, Action.CREATE);
        return classEntity.getId();
    }

    /**
     * Updates an existing class with the provided data.
     *
     * @param updateClassDTO The DTO containing the updated data for the class.
     */
    @Override
    @Transactional
    public void update(String id, UpdateClassDTO updateClassDTO) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        classEntity = ClassConverter.convert(updateClassDTO, classEntity);
        classRepository.save(classEntity);
        pushEvent(classEntity, Action.UPDATE);
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
     * Restores a class and all its parent categories if they were previously deleted or hidden.
     *
     * @param id The unique identifier for the class to be restored.
     */
    @Override
    public void rebound(String id) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        if (EntityStatus.DELETED != classEntity.getStatus()) {
            throw new EntityStatusNotValidException(EntityStatus.DELETED.name());
        }
        classEntity.setStatus(EntityStatus.LIVE);
        classRepository.save(classEntity);
    }

    /**
     * Permanently deletes a class by its unique identifier.
     *
     * @param id The unique identifier for the class to be deleted.
     */
    @Override
    public void delete(String id) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        if (EntityStatus.LIVE != classEntity.getStatus()) {
            throw new EntityStatusNotValidException(EntityStatus.LIVE.name());
        }
        classEntity.setStatus(EntityStatus.DELETED);
        classRepository.save(classEntity);
        pushEvent(classEntity, Action.DELETE);
    }

    /**
     * Unhides an item identified by the given ID.
     *
     * @param id the identifier of the item to unHide; must not be null or empty.
     */
    @Override
    public void unHide(String id) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        if (EntityStatus.HIDDEN != classEntity.getStatus()) {
            throw new EntityStatusNotValidException(EntityStatus.HIDDEN.name());
        }
        classEntity.setStatus(EntityStatus.LIVE);
        classRepository.save(classEntity);
    }

    /**
     * Hides a class, typically to make it inactive or remove it from public view,
     * without permanently deleting it.
     *
     * @param id The unique identifier for the class to be hidden.
     */
    @Override
    @Transactional
    public void hide(String id) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        if (EntityStatus.LIVE != classEntity.getStatus()) {
            throw new EntityStatusNotValidException(EntityStatus.LIVE.name());
        }
        classEntity.setStatus(EntityStatus.HIDDEN);
        classRepository.save(classEntity);
    }

    /**
     * Checks if the provided password is valid for the specified user or entity.
     *
     * @param id       The unique identifier of the user or entity.
     * @param checkPasswordDTO The password to be checked.
     * @return {@code true} if the password is valid, {@code false} otherwise.
     */
    @Override
    public boolean checkPassword(String id, CheckPasswordDTO checkPasswordDTO) {
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        return BCrypt.checkpw(checkPasswordDTO.getPassword(), classEntity.getPassword());
    }

    /**
     * Sets a new password for the specified user or entity.
     *
     * @param id       The unique identifier of the user or entity.
     * @param setPasswordDTO The new password to be set.
     */
    @Override
    @Transactional
    public void setPassword(String id, SetPasswordDTO setPasswordDTO) {
        String hashedPassword = BCrypt.hashpw(setPasswordDTO.getPassword(), BCrypt.gensalt());
        ClassEntity classEntity = classRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id)
        );
        classEntity.setPassword(hashedPassword);
        classRepository.save(classEntity);
    }

    private void pushEvent(ClassEntity classEntity, Action action) {
        AbstractClassApplicationEvent event = switch (action) {
            case CREATE -> new ClassCreatedApplicationEvent();
            case UPDATE -> new ClassUpdatedApplicationEvent();
            case DELETE -> new ClassDeletedApplicationEvent();
        };
        makeEvent(event, classEntity);
        publisher.publishEvent(event);
    }

    private void makeEvent(AbstractClassApplicationEvent event, ClassEntity classEntity) {
        event.setId(classEntity.getId());
        event.setName(classEntity.getName());
        event.setImage(classEntity.getImage());
        event.setEndAt(classEntity.getEndAt());
        event.setHasPassword(classEntity.getPassword() != null);
        event.setCategoryId(classEntity.getCategoryId());
        event.setOwnerId(classEntity.getOwnerId());
        event.setOwnerType(classEntity.getOwnerType().getCode());
        event.setStartAt(classEntity.getStartAt());
        event.setStatus(classEntity.getStatus().getCode());

        // TODO: set TransactionId
        // event.setTransactionId();
    }

}
