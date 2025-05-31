package com.kduytran.categoryservice.service;

import com.kduytran.categoryservice.entity.CategoryEntity;
import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.olpcommon.validation.CommonValidatorService;
import com.kduytran.olpcommon.validation.FieldValueExists;
import com.kduytran.olpcommon.validation.ValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryValidationService implements FieldValueExists {

    private final CategoryRepository categoryRepository;

    public static final List<ValidationRule> createValidationRules = List.of(
            ValidationRule.builder().fieldName("name").build(),
            ValidationRule.builder().fieldName("code").build()
    );

    public static final List<ValidationRule> updateValidationRules = List.of(
            ValidationRule.builder().fieldName("name").isUpdate(true).build(),
            ValidationRule.builder().fieldName("code").isUpdate(true).build()
    );

    @Override
    public boolean exists(Object fieldValue, String fieldName) {
        Specification<CategoryEntity> specification =
                Specification.where(CommonValidatorService.fieldEquals(fieldName, fieldValue, null));
        return categoryRepository.count(specification) > 0;
    }

    @Override
    public boolean exists(Object fieldValue, String fieldName, Object id) {
        Specification<CategoryEntity> specification =
                Specification.where(CommonValidatorService.fieldEquals(fieldName, fieldValue, String.valueOf(id)));
        return categoryRepository.count(specification) > 0;
    }
}
