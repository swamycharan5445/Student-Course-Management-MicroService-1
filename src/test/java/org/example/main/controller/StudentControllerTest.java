package org.example.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.main.dto.StudentDTO;
import org.example.main.feign.SpringFeignClient;
import org.example.main.proxy.CourseRecommendationProxy;
import org.example.main.repository.CourseJdbcRepository;
import org.example.main.repository.StudentRepo;
import org.example.main.service.CourseService;
import org.example.main.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {StudentController.class})
class StudentControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private StudentRepo studentRepo;

    @MockitoBean
    private SpringFeignClient springFeignClient;

    @MockitoBean
    private CourseRecommendationProxy courseRecommendationProxy;


    @Autowired
    private ObjectMapper objectMapper;

    StudentDTO studentDTO;

    @BeforeEach
    void setUp()
    {
        studentDTO=new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setName("charan");
        studentDTO.setAge(23);
        studentDTO.setEmail("charan@gmail.com");
    }

    @Test
    void addStudentData() throws Exception
    {
        when(studentService.addStudent(studentDTO)).thenReturn("Student added successfully");
        when(studentRepo.findByEmail(studentDTO.getEmail())).thenReturn(null);

        mockMvc.perform(post("/student/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student added successfully"));
    }


    @Test
    void updateStudentData() throws Exception
    {
        when(studentService.updateStudent(studentDTO)).thenReturn("Student updated successfully");

        mockMvc.perform(patch("/student/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student updated successfully"));


    }
}