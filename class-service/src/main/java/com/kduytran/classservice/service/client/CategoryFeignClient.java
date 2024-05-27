package com.kduytran.classservice.service.client;

import com.kduytran.classservice.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category")
public interface CategoryFeignClient {

    @GetMapping("/api/v1/categories/{id}")
    ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") String id);

}
