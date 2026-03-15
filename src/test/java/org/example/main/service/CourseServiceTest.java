package org.example.main.service;

import org.example.main.dto.CourseDTO;
import org.example.main.entity.Course;
import org.example.main.entity.Instructor;
import org.example.main.exception.InstructorNotFoundException;
import org.example.main.repository.CourseJdbcRepository;
import org.example.main.repository.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private CourseJdbcRepository  courseJdbcRepository;

    @InjectMocks
    private CourseService courseService;

    private CourseDTO courseDTO;
    private Instructor instructor;

    @BeforeEach
    void setUp()
    {
        instructor = new Instructor();
        instructor.setId(1);
        instructor.setName("John Doe");

        courseDTO = new CourseDTO();
        courseDTO.setCourseId("J001");
        courseDTO.setCourseName("Java");
        courseDTO.setCourseFee(5000.00);
        courseDTO.setModeType("Online");
        courseDTO.setInstructor(instructor);
    }


    @Test
    void testAddCourseAlreadyExists()
    {
        Course existingCourse = new Course();
//        existingCourse.setCourseId(courseDTO.getCourseId());

        when(courseRepo.findByCourseId(courseDTO.getCourseId())).thenReturn(existingCourse);

        String result = courseService.addCourse(courseDTO);

        assertEquals(courseDTO.getCourseId()+" already exists", result);
    }


    @Test
    void testAddCourseSuccess()
    {
        when(courseRepo.findByCourseId(courseDTO.getCourseId())).thenReturn(null);

        String result = courseService.addCourse(courseDTO);

        assertEquals("Course added successfully", result);
        verify(courseRepo, times(1)).save(any(Course.class));
    }


    @Test
    void testAddCourseInstructorNotFound()
    {
        courseDTO.setInstructor(null);
        when(courseRepo.findByCourseId(courseDTO.getCourseId())).thenReturn(null);

        InstructorNotFoundException exception = assertThrows(
                InstructorNotFoundException.class,
                () -> courseService.addCourse(courseDTO)
        );

        assertEquals("Instructor not found", exception.getMessage());
//        verify(courseRepo, never()).save(any());
    }
    @Test
    void testUpdateCourseSuccess()
    {
        Course existingCourse = new Course();
//        existingCourse.setCourseId(courseDTO.getCourseId());
//        existingCourse.setCourseName(courseDTO.getCourseName());
//        existingCourse.setInstructor(instructor);
//        existingCourse.setModeType(courseDTO.getModeType());
//        existingCourse.setCourseFee(courseDTO.getCourseFee());

        when(courseJdbcRepository.findByCourseId(courseDTO.getCourseName())).thenReturn(existingCourse);

        courseDTO.setCourseFee(10000.00);
        courseDTO.setModeType("Offline");

        String result = courseService.updateCourse(courseDTO);

        assertEquals("Course updated successfully", result);
        verify(courseRepo, times(1)).save(existingCourse);

        assertEquals(courseDTO.getCourseName(), existingCourse.getCourseName());
        assertEquals(courseDTO.getCourseFee(), existingCourse.getCourseFee());
        assertEquals(courseDTO.getModeType(), existingCourse.getModeType());
        assertEquals(instructor, existingCourse.getInstructor());
    }

    @Test
    void testUpdateCourseNotFound()
    {
        when(courseJdbcRepository.findByCourseId(courseDTO.getCourseName())).thenReturn(null);
        String result = courseService.updateCourse(courseDTO);
        assertEquals("Course not found with id: "+courseDTO.getCourseName(), result);

    }
}
