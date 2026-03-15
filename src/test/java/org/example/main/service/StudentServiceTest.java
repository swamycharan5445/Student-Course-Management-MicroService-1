package org.example.main.service;

import org.example.main.dto.StudentDTO;
import org.example.main.entity.Student;
import org.example.main.exception.StudentNotFoundException;
import org.example.main.repository.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest
{
    @Mock
    private StudentRepo studentRepo;
    @Mock
    IdGenerate idGenerate;

    @InjectMocks
    private StudentService studentService;

    StudentDTO studentDTO;
    StudentDTO studentDTO1;


    @BeforeEach
    void setUp()
    {
        studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setName("charan");
        studentDTO.setAge(21);
        studentDTO.setEmail("charan@gmail.com");

        studentDTO1 = new StudentDTO();
        studentDTO1.setId(2);
        studentDTO1.setName("ram");
        studentDTO1.setAge(21);
        studentDTO1.setEmail("ram@gmail.com");

    }

    @Test
    void addStudentEmailExists()
    {
        Student student = new Student();
        when(studentRepo.findByEmail(anyString())).thenReturn(student);
        String result = studentService.addStudent(studentDTO1);

        assertEquals("Student already exists with email: ",result);

    }

    @Test
    void addStudentEmailNotExists()
    {
        when(studentRepo.findByEmail(anyString())).thenReturn(null);
        String result = studentService.addStudent(studentDTO);
        assertEquals("Student added successfully",result);
    }

    @Test
    void updateStudentIdNotFound()
    {
        when(studentRepo.findById(studentDTO.getId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(studentDTO));
    }

    @Test
    void updateStudentIdExists()
    {
        Student student = new Student();
        when(studentRepo.findById(studentDTO.getId())).thenReturn(Optional.of(student));

        studentDTO.setAge(student.getAge());

        String result = studentService.updateStudent(studentDTO);
        assertEquals("Student updated successfully",result);
        assertEquals(studentDTO.getAge(),student.getAge());

    }
}