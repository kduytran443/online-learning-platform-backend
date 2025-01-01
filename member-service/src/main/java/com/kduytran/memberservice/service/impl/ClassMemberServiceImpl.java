package com.kduytran.memberservice.service.impl;

import com.kduytran.memberservice.converter.ClassMemberConverter;
import com.kduytran.memberservice.dto.ClassDTO;
import com.kduytran.memberservice.dto.ClassMemberRequestDTO;
import com.kduytran.memberservice.dto.UserClassesResponseDTO;
import com.kduytran.memberservice.entity.ClassMemberEntity;
import com.kduytran.memberservice.repository.ClassMemberRepository;
import com.kduytran.memberservice.service.ClassMemberService;
import com.kduytran.memberservice.service.client.ClassQueryFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClassMemberServiceImpl implements ClassMemberService {

    private final ClassMemberRepository classMemberRepository;
    private final ClassQueryFeignClient classQueryFeignClient;

    @Override
    public UUID joinClass(ClassMemberRequestDTO dto) {
        ClassMemberEntity entity = ClassMemberConverter.convert(dto, new ClassMemberEntity());
        entity.setJoinedAt(LocalDateTime.now());
        entity = classMemberRepository.save(entity);
        // todo: Call API to KeyCloak
        return entity.getId();
    }

    @Override
    public UserClassesResponseDTO getClassesByUserId(UUID userId) {
        Set<UUID> classIds = classMemberRepository.findAllClassIdsByUserId(userId);
        List<ClassDTO> classDTOs = fetchAllClassesByUserId(classIds);

        return UserClassesResponseDTO.builder()
                .userId(userId)
                .classes(classDTOs)
                .build();
    }

    @Retry(name = "feignClientRetry")
    @CircuitBreaker(name = "feignClientCircuitBreaker")
    private List<ClassDTO> fetchAllClassesByUserId(Set<UUID> classIds) {
        log.info("Fetching classes by IDs: {}", classIds);
        if (classIds.isEmpty()) {
            return Collections.emptyList();
        }
        return classQueryFeignClient.findAllClassesByIds(new ArrayList<>(classIds));
    }
}
