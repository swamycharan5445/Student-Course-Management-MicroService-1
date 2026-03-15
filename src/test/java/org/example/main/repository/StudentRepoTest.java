package org.example.main.repository;

import org.example.main.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepoTest
{

    @Autowired
    private StudentRepo studentRepo;

    Student student;
    Student student2;

    @BeforeEach
    void setUp()
    {
        student = new Student();
        student.setId(1);
        student.setEmail("charan@gmail.com");

        student2 = new Student();
        student2.setId(2);
    }

    @Test
    void findByEmailWhenEmailExists()
    {

        studentRepo.save(student);
//        studentRepo.save(student2);

        Student found = studentRepo.findByEmail(student.getEmail());

        assertNotNull(found.getEmail());
        assertEquals("charan@gmail.com", found.getEmail());
    }

    @Test
    void findByEmailWhenEmailNotExists()
    {
        Student found = studentRepo.findByEmail("swamy@gmail.com");
        assertNull(found);
    }
    
    @Test
    void findByIdWhenIdExists()
    {
        studentRepo.save(student);
        studentRepo.save(student2);
        int maxId = studentRepo.maxStudentId();
        assertEquals(student2.getId(), maxId);
    }

}
