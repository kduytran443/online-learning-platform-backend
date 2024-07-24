package com.kduytran.classresourceservice.service;
import static org.junit.jupiter.api.Assertions.*;
import com.kduytran.classresourceservice.dto.CreateTopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class TopicServiceTest {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ILessonService lessonService;

    @Autowired
    private TopicServiceImpl topicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTopic() {
        CreateTopicDTO dto = new CreateTopicDTO();
        dto.setClassId(UUID.randomUUID().toString());
        dto.setName("New live Topic");
        dto.setStatus(EntityStatus.LIVE);

        UUID savedId = topicService.create(dto);
        assertNotNull(savedId);
        TopicEntity savedEntity = topicRepository.findById(savedId).get();
        assertEquals(dto.getName(), savedEntity.getName());
        assertEquals(1, savedEntity.getSeq());
        assertEquals(EntityStatus.LIVE, savedEntity.getStatus());
    }

    @Test
    @Sql("/topic.sql")
    public void testUpdateTopic() {
        UpdateTopicDTO dto = new UpdateTopicDTO();
        dto.setId("57161f1b-341d-4348-add6-4c3b558de4fd");
        dto.setName("Update name");

        UUID id = UUID.fromString("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity savedEntity = topicRepository.findById(id).get();
        assertNotNull(savedEntity);

    }

}
