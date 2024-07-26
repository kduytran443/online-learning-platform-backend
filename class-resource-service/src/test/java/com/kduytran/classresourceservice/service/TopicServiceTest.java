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
        UUID id = UUID.fromString("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity oldEntity = topicRepository.findById(id).get();
        assertNotNull(oldEntity);

        UpdateTopicDTO dto = new UpdateTopicDTO();
        dto.setId("57161f1b-341d-4348-add6-4c3b558de4fd");
        dto.setName("Update name");
        topicService.update(dto);

        TopicEntity updatedEntity = topicRepository.findById(id).get();
        assertNotNull(updatedEntity);
        assertEquals(updatedEntity.getId().toString(), dto.getId().toString());
        assertEquals(updatedEntity.getName(), dto.getName());
        if (dto.getStatus() != null) {
            assertEquals(updatedEntity.getStatus(), dto.getStatus());
        }
    }

    @Test
    @Sql("/topic.sql")
    public void testDelete() {
        UUID id = UUID.fromString("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity topicEntity = topicRepository.findById(id).get();
        assertNotNull(topicEntity);

        topicService.delete("57161f1b-341d-4348-add6-4c3b558de4fd");
        topicEntity = topicRepository.findById(id).get();
        assertEquals(EntityStatus.DELETED, topicEntity.getStatus());
    }

    @Test
    @Sql("/topic.sql")
    public void testHide() {
        UUID id = UUID.fromString("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity topicEntity = topicRepository.findById(id).get();
        assertNotNull(topicEntity);

        topicService.hide("57161f1b-341d-4348-add6-4c3b558de4fd");
        topicEntity = topicRepository.findById(id).get();
        assertEquals(EntityStatus.HIDDEN, topicEntity.getStatus());
    }

    @Test
    @Sql("/topic.sql")
    public void testUpdateNextSeq() {
        UUID id = UUID.fromString("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity topicEntity = topicRepository.findById(id).get();
        assertNotNull(topicEntity);

        topicService.updateNextSeq("57161f1b-341d-4348-add6-4c3b558de4fd");
        TopicEntity updatedTopicEntity = topicRepository.findById(id).get();
        assertTrue(updatedTopicEntity.getSeq() == topicEntity.getSeq() + 1
                            || updatedTopicEntity.getSeq() == 1);
    }

}
