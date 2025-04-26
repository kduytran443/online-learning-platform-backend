package com.kduytran.categoryservice.runner;

import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.categoryservice.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDatabaseRunner {

    @Bean
    CommandLineRunner initDatabase(CategoryService categoryService, CategoryRepository categoryRepository) {
        return args -> {
        };
    }
}
