package org.example.main.repository;

import org.example.main.entity.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InstructorRepoTest
{
    @Autowired
    private InstructorRepo instructorRepo;
    Instructor instructor;
    Instructor instructor2;

    @BeforeEach
    void setUp()
    {
        instructor = new Instructor();
        instructor.setId(1);
        instructor.setName("charan");
        instructor2 = new Instructor();
        instructor2.setId(2);

    }

    @Test
    void maxInstructorId()
    {
        instructorRepo.save(instructor);
        instructorRepo.save(instructor2);
        int maxId = instructorRepo.maxInstructorId();
        assertEquals(instructor2.getId(), maxId);

    }

    @Test
    void findByName()
    {
        instructorRepo.save(instructor);
        Instructor foundInstructor = instructorRepo.findByName(instructor.getName());

        assertNotNull(foundInstructor.getName());
        assertEquals("charan", foundInstructor.getName());

    }
}