package com.kduytran.classresourceservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.classresourceservice.constant.ResponseConstant;
import com.kduytran.classresourceservice.dto.*;
import com.kduytran.classresourceservice.entity.EntityStatus;
import com.kduytran.classresourceservice.service.ILessonService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LessonControllerTest {

    @MockBean
    private ILessonService lessonService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLesson() throws Exception {
        CreateLessonDTO dto = new CreateLessonDTO();
        dto.setName("Lesson name");
        dto.setStatus(EntityStatus.LIVE);
        dto.setDescription("Description");
        dto.setTopicId("topicId");

        UUID id = UUID.randomUUID();
        when(lessonService.create(any())).thenReturn(id);
        mockMvc.perform(MockMvcRequestBuilders.post(TestUtils.getLessonUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_201))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testUpdateLesson() throws Exception {
        UpdateLessonDTO dto = new UpdateLessonDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setStatus(EntityStatus.LIVE);
        dto.setName("Name");
        dto.setDescription("Description");
        dto.setTopicId(UUID.randomUUID().toString());

        doNothing().when(lessonService).update(any());
        mockMvc.perform(MockMvcRequestBuilders.put(TestUtils.getLessonUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testDeleteLesson() throws Exception {
        doNothing().when(lessonService).delete(any());
        mockMvc.perform(MockMvcRequestBuilders.delete(TestUtils.getLessonUrl(UUID.randomUUID().toString())))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testHideLesson() throws Exception {
        doNothing().when(lessonService).hide(any());
        mockMvc.perform(MockMvcRequestBuilders.put(TestUtils.getLessonUrl(UUID.randomUUID().toString(),
                        "hide")))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testUpdateNextSeq() throws Exception {
        doNothing().when(lessonService).updateNextSeq(any());
        mockMvc.perform(MockMvcRequestBuilders.put(TestUtils.getLessonUrl(UUID.randomUUID().toString(),
                        "next-seq")))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testUpdatePreviousSeq() throws Exception {
        doNothing().when(lessonService).updatePreviousSeq(any());
        mockMvc.perform(MockMvcRequestBuilders.put(TestUtils.getLessonUrl(UUID.randomUUID().toString(),
                        "previous-seq")))
                .andExpect(jsonPath("$.statusCode").value(ResponseConstant.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(ResponseConstant.MESSAGE_200));
    }

    @Test
    public void testFindAllByTopicId() throws Exception {
        LessonDTO lesson1 = new LessonDTO();
        lesson1.setId("ID-1");
        lesson1.setSeq(1);
        lesson1.setName("Name");
        lesson1.setDescription("Description");

        LessonDTO lesson2 = new LessonDTO();
        lesson2.setId("ID-2");
        lesson2.setSeq(2);
        lesson2.setName("Name2");
        lesson2.setDescription("Description2");

        LessonDTO lesson3 = new LessonDTO();
        lesson3.setId("ID-3");
        lesson3.setSeq(3);
        lesson3.setName("Name3");
        lesson3.setDescription("Description3");

        List<LessonDTO> lessonDTOs = List.of(lesson1, lesson2, lesson3);
        when(lessonService.findAllByTopicId(any(), any())).thenReturn(lessonDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get(TestUtils.getLessonUrl())
                        .param("statuses", "LIVE,HIDDEN")
                        .param("topicId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(lessonDTOs.size()))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(lessonDTOs)));
    }

}
