package com.kduytran.classservice.service.impl;

import com.kduytran.classservice.entity.ClassEntity;
import com.kduytran.classservice.dto.ClassDetailsDTO;
import com.kduytran.classservice.dto.SaveClassDTO;
import com.kduytran.classservice.exception.ResourceNotFoundException;
import com.kduytran.classservice.mapper.ClassMapper;
import com.kduytran.classservice.service.ClassService;
import com.kduytran.classservice.service.S3Service;
import com.kduytran.classservice.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.Instant;

@Transactional
@Service
@RequiredArgsConstructor
class ClassServiceImpl implements ClassService {

    private final ClassMapper classMapper;
    private final S3Service s3Service;
    private final ClassRepository classRepository;

    /**
     * Creates a new class based on the provided data.
     */
    @Override
    public UUID create(SaveClassDTO saveClassDTO, MultipartFile thumbnail, MultipartFile banner) {
        ClassEntity classEntity = classMapper.toEntity(saveClassDTO);

        // Use S3 to store files
        String thumbnailKey = s3Service.uploadFile(thumbnail);
        String bannerKey = s3Service.uploadFile(banner);

        classEntity.setThumbnailImageKey(thumbnailKey);
        classEntity.setBannerImageKey(bannerKey);
        classEntity = classRepository.save(classEntity);

        return classEntity.getId();
    }

    /**
     * Updates an existing class with the provided data.
     *
     * @param saveClassDTO The DTO containing the updated data for the class.
     */
    @Override
    public void update(UUID id, SaveClassDTO saveClassDTO) {
        ClassEntity classEntity = classRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("class", "id", id.toString())
        );
        classMapper.mapEntity(saveClassDTO, classEntity);
        classRepository.save(classEntity);
    }

    /**
     * Permanently deletes a class by its unique identifier.
     *
     * @param id The unique identifier for the class to be deleted.
     */
    @Override
    public void delete(UUID id) {
        boolean existing = classRepository.existsById(id);
        if (!existing) {
            throw new ResourceNotFoundException("class", "id", id.toString());
        }
        ClassEntity classEntity = classRepository.getReferenceById(id);
        classEntity.setArchivedAt(Instant.now());
        classRepository.save(classEntity);
    }

    @Override
    public ClassDetailsDTO get(UUID id) {
        return null;
    }
}
