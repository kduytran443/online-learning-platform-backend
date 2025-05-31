package com.kduytran.memberservice.service.client;

import com.kduytran.memberservice.dto.ClassDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
class ClassQueryFeignClientFallback implements ClassQueryFeignClient {

    @Override
    public List<ClassDTO> findAllClassesByIds(List<UUID> ids) {
        log.error("Failed to fetch class details for IDs: {}. Returning empty list as fallback.", ids);
        return List.of();
    }
}
