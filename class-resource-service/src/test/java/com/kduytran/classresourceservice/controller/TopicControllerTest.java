package com.kduytran.classresourceservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.classresourceservice.constant.RequestConstant;
import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.CreateTopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.entity.TopicEntity;
import com.kduytran.classresourceservice.repository.TopicRepository;
import com.kduytran.classresourceservice.service.ITopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TopicControllerTest {

    @MockBean
    private ITopicService topicService;

    @MockBean
    private TopicRepository topicRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTopic() throws Exception {
        CreateTopicDTO dto = new CreateTopicDTO();
        dto.setClassId("some-class-id");
        dto.setName("New Topic");
        dto.setStatus(EntityStatus.LIVE);

        UUID id = UUID.randomUUID();
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(id);
        when(topicService.create(any(CreateTopicDTO.class))).thenReturn(id);

        mockMvc.perform(MockMvcRequestBuilders.post(RequestConstant.TOPIC_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_201))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testUpdateTopic() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateTopicDTO dto = new UpdateTopicDTO();
        dto.setClassId("some-class-id");
        dto.setName("Update Topic");
        dto.setId(id.toString());

        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(id);
        when(topicRepository.findById(any(UUID.class))).thenReturn(Optional.of(topicEntity));
        doNothing().when(topicService).update(any(UpdateTopicDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.put(RequestConstant.TOPIC_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testDeleteTopic() throws Exception {
        UUID id = UUID.randomUUID();
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(id);
        when(topicRepository.findById(any(UUID.class))).thenReturn(Optional.of(topicEntity));
        when(topicRepository.findAllByClassIdAndSeqGreaterThanEqualOrderBySeqAsc(
                any(UUID.class),
                any(Integer.class))
        ).thenReturn(new ArrayList<>());
        doNothing().when(topicService).delete(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s",
                                RequestConstant.TOPIC_CONTROLLER_URL, id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testHideTopic() throws Exception {
        UUID id = UUID.randomUUID();
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(id);
        when(topicRepository.findById(any(UUID.class))).thenReturn(Optional.of(topicEntity));
        doNothing().when(topicService).hide(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s",
                        RequestConstant.TOPIC_CONTROLLER_URL, id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testUpdateNextSeq() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(topicService).updateNextSeq(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/next-seq/%s", RequestConstant.TOPIC_CONTROLLER_URL, id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

}
