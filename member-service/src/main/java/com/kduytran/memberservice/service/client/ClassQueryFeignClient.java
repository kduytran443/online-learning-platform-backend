package com.kduytran.memberservice.service.client;

import com.kduytran.memberservice.dto.ClassDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "class-query", fallback = ClassQueryFeignClientFallback.class, primary = false)
public interface ClassQueryFeignClient {

    @GetMapping("/api/v1/class-queries/list-by-ids")
    List<ClassDTO> findAllClassesByIds(@RequestParam List<UUID> ids);
}
