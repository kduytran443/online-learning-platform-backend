package com.kduytran.classresourceservice.service;

import static org.junit.jupiter.api.Assertions.*;
import com.kduytran.classresourceservice.dto.CreateLessonDTO;
import com.kduytran.classresourceservice.dto.UpdateLessonDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.LessonEntity;
import com.kduytran.classresourceservice.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {
        "/topic.sql",
        "/lesson.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class LessonServiceTest {

    @Autowired
    private ILessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    public void testCreateLesson() {
        CreateLessonDTO dto = new CreateLessonDTO();
        dto.setTopicId("5a922f8f-9085-4344-bacf-75c44ab8f5a6");
        dto.setName("Name");
        dto.setDescription("Description");
        dto.setStatus(EntityStatus.LIVE);

        UUID id = lessonService.create(dto);
        LessonEntity lessonEntity = lessonRepository.findById(id).get();
        assertEquals(dto.getName(), lessonEntity.getName());
        assertEquals(dto.getStatus(), lessonEntity.getStatus());
        assertEquals(dto.getDescription(), lessonEntity.getDescription());
        assertEquals(dto.getTopicId().toString(), lessonEntity.getTopic().getId().toString());

        long lessonSize = lessonRepository.countAllByTopicIdAndStatusIn(lessonEntity.getTopic().getId(),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        System.out.println(lessonSize);
    }

    @Test
    public void testUpdateLesson() {
        UpdateLessonDTO dto = new UpdateLessonDTO();
        dto.setId("ffc5e6c3-34b9-4d13-94b2-486b789cbae3");
        dto.setStatus(EntityStatus.HIDDEN);
        dto.setName("Updated name");
        dto.setDescription("New description");

        lessonService.update(dto);
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString("ffc5e6c3-34b9-4d13-94b2-486b789cbae3")).get();
        assertEquals(dto.getStatus(), lessonEntity.getStatus());
        assertEquals(dto.getName(), lessonEntity.getName());
        assertEquals(dto.getDescription(), lessonEntity.getDescription());
    }

    @Test
    public void testDeleteLesson() {
        lessonService.delete("ffc5e6c3-34b9-4d13-94b2-486b789cbae3");
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString("ffc5e6c3-34b9-4d13-94b2-486b789cbae3")).get();
        assertEquals(EntityStatus.DELETED, lessonEntity.getStatus());
    }

    @Test
    public void testHideLesson() {
        lessonService.hide("ffc5e6c3-34b9-4d13-94b2-486b789cbae3");
        LessonEntity lessonEntity = lessonRepository.findById(UUID.fromString("ffc5e6c3-34b9-4d13-94b2-486b789cbae3")).get();
        assertEquals(EntityStatus.HIDDEN, lessonEntity.getStatus());
    }

    @Test
    public void testUpdateNextSeq() {
        UUID id = UUID.fromString("2c63eed0-34bd-45b1-920f-60a4eee1d95e");
        LessonEntity lessonEntity = lessonRepository.findById(id).get();
        lessonService.updateNextSeq(id.toString());

        LessonEntity updatedLessonEntity = lessonRepository.findById(UUID.fromString("2c63eed0-34bd-45b1-920f-60a4eee1d95e")).get();
        assertTrue(updatedLessonEntity.getSeq() == lessonEntity.getSeq() + 1 ||
                updatedLessonEntity.getSeq() == 1);
    }

    @Test
    public void testUpdatePreviousSeq() {
        UUID id = UUID.fromString("2c63eed0-34bd-45b1-920f-60a4eee1d95e");
        LessonEntity lessonEntity = lessonRepository.findById(id).get();
        lessonService.updatePreviousSeq(id.toString());
        long lessonLength = lessonRepository.countAllByTopicIdAndStatusIn(lessonEntity.getTopic().getId(),
                List.of(EntityStatus.LIVE, EntityStatus.HIDDEN));
        LessonEntity updatedLessonEntity = lessonRepository.findById(UUID.fromString("2c63eed0-34bd-45b1-920f-60a4eee1d95e")).get();
        assertTrue(updatedLessonEntity.getSeq() == lessonEntity.getSeq() - 1 ||
                updatedLessonEntity.getSeq() == lessonLength);
    }

}
