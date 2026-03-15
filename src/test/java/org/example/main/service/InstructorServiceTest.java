package org.example.main.service;

import org.example.main.dto.InstructorDTO;
import org.example.main.entity.Instructor;
import org.example.main.repository.InstructorRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest
{
    @Mock
    IdGenerate idGenerate;

    @Mock
    InstructorRepo instructorRepo;

    @InjectMocks
    InstructorService instructorService;

    @Test
    public void addInstructorTest()
    {
        InstructorDTO instructordto = new InstructorDTO();
        instructordto.setId(1);
        instructordto.setName("charan");
        instructordto.setExpertise(new HashSet<>(List.of("java","springboot")));

//        when(idGenerate.getInstructor_id()).thenReturn(1);

        String addedInstructor = instructorService.addInstructor(instructordto);

        assertEquals("Instructor added successfully!", addedInstructor);

        verify(instructorRepo).save(any(Instructor.class));

    }
}