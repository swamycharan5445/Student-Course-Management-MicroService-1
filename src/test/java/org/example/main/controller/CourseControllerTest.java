package org.example.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.main.dto.CourseDTO;
import org.example.main.entity.Course;
import org.example.main.repository.CourseJdbcRepository;
import org.example.main.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {CourseController.class})
class CourseControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private CourseJdbcRepository courseJdbcRepository;

    @Autowired
    private ObjectMapper objectMapper;

    CourseDTO courseDTO;

    @BeforeEach
    void setUp()
    {
        courseDTO = new CourseDTO();
        courseDTO.setCourseId("J001");
        courseDTO.setCourseName("Java Full Stack");
        courseDTO.setCourseFee(19999.99);
        courseDTO.setModeType("Online");

    }

    @Test
    void addCourseData() throws Exception
    {
        when(courseService.addCourse(courseDTO)).thenReturn("Course added successfully");

        mockMvc.perform(post("/course/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Course added successfully"));
    }

    @Test
    void updateCourseData() throws Exception
    {
        Course course = new Course();
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn("Course updated successfully");
        when(courseJdbcRepository.findByCourseId(courseDTO.getCourseId())).thenReturn(course);

        mockMvc.perform(patch("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Course updated successfully"));
    }

}