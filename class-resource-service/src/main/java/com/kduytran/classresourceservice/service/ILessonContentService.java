package com.kduytran.classresourceservice.service;

import com.kduytran.classresourceservice.dto.LessonContentDTO;

import java.util.UUID;

public interface ILessonContentService {

    UUID edit(LessonContentDTO dto);
    LessonContentDTO getById(String id);

}
