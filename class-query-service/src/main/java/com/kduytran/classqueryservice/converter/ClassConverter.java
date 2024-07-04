package com.kduytran.classqueryservice.converter;

import com.kduytran.classqueryservice.document.ClassDocument;
import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.utils.StreamUtil;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ClassConverter {

    public static ClassDocument convert(@NonNull ClassDTO dto, @NonNull ClassDocument document) {
        document.setName(dto.getName());
        document.setImage(dto.getImage());
        document.setStartAt(dto.getStartAt());
        document.setEndAt(dto.getEndAt());
        document.setStatus(dto.getStatus());
        document.setCategoryId(dto.getCategoryId());
        document.setCategoryCode(dto.getCategoryCode());
        document.setCategoryName(dto.getCategoryName());
        document.setOwnerType(dto.getOwnerType());
        document.setOwnerId(dto.getOwnerId());
        document.setOwnerName(dto.getOwnerName());
        return document;
    }

    public static ClassDTO convert(ClassDocument document) {
        ClassDTO dto = new ClassDTO();
        dto.setId(document.getId());
        dto.setName(document.getName());
        dto.setImage(document.getImage());
        dto.setStartAt(document.getStartAt());
        dto.setEndAt(document.getEndAt());
        dto.setStatus(document.getStatus());
        dto.setCategoryId(document.getCategoryId());
        dto.setCategoryCode(document.getCategoryCode());
        dto.setCategoryName(document.getCategoryName());
        dto.setOwnerType(document.getOwnerType());
        dto.setOwnerId(document.getOwnerId());
        dto.setOwnerName(document.getOwnerName());
        dto.setNumberOfReviews(document.getNumberOfReviews());
        dto.setAverageRating(document.getAverageRating());
        dto.setOwnerName(document.getOwnerName());
        return dto;
    }

    public static List<ClassDTO> convert(@NonNull Iterable<ClassDocument> documents) {
        return StreamUtil.iterableToStream(documents).map(ClassConverter::convert).collect(Collectors.toList());
    }

}
