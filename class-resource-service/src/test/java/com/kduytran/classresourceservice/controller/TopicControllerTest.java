package com.kduytran.classresourceservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.CreateTopicDTO;
import com.kduytran.classresourceservice.dto.TopicDTO;
import com.kduytran.classresourceservice.dto.UpdateTopicDTO;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.service.ITopicService;
import com.kduytran.classresourceservice.utils.TestUtils;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TopicControllerTest {

    @MockBean
    private ITopicService topicService;

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
        when(topicService.create(any(CreateTopicDTO.class))).thenReturn(id);

        mockMvc.perform(MockMvcRequestBuilders.post(TestUtils.getTopicUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_201))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testUpdateTopic() throws Exception {
        UpdateTopicDTO dto = new UpdateTopicDTO();
        dto.setClassId("some-class-id");
        dto.setName("Update Topic");
        dto.setId(UUID.randomUUID().toString());

        doNothing().when(topicService).update(any(UpdateTopicDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.put(TestUtils.getTopicUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testDeleteTopic() throws Exception {
        doNothing().when(topicService).delete(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete(TestUtils.getTopicUrl(UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testHideTopic() throws Exception {
        doNothing().when(topicService).hide(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put(
                TestUtils.getTopicUrl("hide", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testUpdateNextSeq() throws Exception {
        doNothing().when(topicService).updateNextSeq(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put(
                TestUtils.getTopicUrl("next-seq", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testUpdatePrevSeq() throws Exception {
        doNothing().when(topicService).updatePreviousSeq(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put(
                        TestUtils.getTopicUrl("previous-seq", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testFindAllByClassId() throws Exception {
        List<TopicDTO> dtoList = Arrays.asList(
                new TopicDTO("id-1", "name", EntityStatus.LIVE, "classId", new ArrayList<>()),
                new TopicDTO("id-2", "name", EntityStatus.LIVE, "classId", new ArrayList<>()),
                new TopicDTO("id-3", "name", EntityStatus.LIVE, "classId", new ArrayList<>())
        );

        when(topicService.findAllByClassId(anyString(), anyList())).thenReturn(dtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(
                        TestUtils.getTopicUrl("class", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(dtoList)));
    }

}
