package org.example.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.main.dto.InstructorDTO;
import org.example.main.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = InstructorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {InstructorController.class})
class InstructorControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    InstructorDTO instructorDTO;

    @BeforeEach
    void setUp()
    {
        instructorDTO=new InstructorDTO();
        instructorDTO.setId(1);
        instructorDTO.setName("charan");
        instructorDTO.setExpertise(Set.of("java","SpringBoot"));


    }

    @Test
    void addInstructorData() throws Exception
    {
        when(instructorService.addInstructor(instructorDTO)).thenReturn("Instructor added successfully");
        mockMvc.perform(post("/instructor/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instructorDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Instructor added successfully"));
    }
}