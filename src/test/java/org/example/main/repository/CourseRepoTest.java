package org.example.main.repository;

import org.example.main.entity.Course;
import org.example.main.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepoTest
{
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private StudentRepo studentRepo;

    Course course;
    Student student;

    @BeforeEach
    void setUp()
    {

        student = new Student();
        student.setId(1);
        student.setName("Charan");
        student.setEmail("charan@gmail.com");

        course = new Course();
        course.setCourseId("J001");
        course.setCourseName("Java Full Stack");
    }

    @Test
    void findByCourseId()
    {
        courseRepo.save(course);
        Course course1 = courseRepo.findByCourseId(course.getCourseId());
        assertNotNull(course1.getCourseId());
        assertEquals(course.getCourseId(), course1.getCourseId());
    }

    @Test
    void deleteByCourseId()
    {
        courseRepo.save(course);
        courseRepo.deleteByCourseId(course.getCourseId());
//        verify(courseRepo).deleteByCourseId(any(String.class));
        assertNull(courseRepo.findByCourseId(course.getCourseId()));
    }

    @Test
    void findByCourseName()
    {
        courseRepo.save(course);
        Course byCourseName = courseRepo.findByCourseName(course.getCourseName());
        assertNotNull(byCourseName.getCourseName());
        assertEquals(course.getCourseName(),byCourseName.getCourseName());

    }

    @Test
    void getCoursesWithStudents()
    {

        student.setCourses(new ArrayList<>());
        student.getCourses().add(course);

        course.setStudents(new ArrayList<>());
        course.getStudents().add(student);

        studentRepo.save(student);
        courseRepo.save(course);

        List<Course> coursesWithStudents = courseRepo.getCoursesWithStudents();

        assertNotNull(coursesWithStudents);
        assertFalse(coursesWithStudents.isEmpty());

        Course savedCourse = coursesWithStudents.get(0);
        assertNotNull(savedCourse.getStudents());
        assertFalse(savedCourse.getStudents().isEmpty());
        assertEquals("Charan", savedCourse.getStudents().get(0).getName());
    }


    @Test
    void getStudentCountByCourseId()
    {
        Student student = new Student();
        student.setId(1);
        student.setName("Charan");
        student.setEmail("charan@gmail.com");

        Course course = new Course();
        course.setCourseId("C101");
        course.setCourseName("Spring Boot");

        student.setCourses(List.of(course));

        studentRepo.save(student);
        courseRepo.save(course);

        Long count = courseRepo.getStudentCountByCourseId("C101");

        assertEquals(1L, count);
    }

}