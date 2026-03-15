package org.example.main.service;


import org.example.main.repository.InstructorRepo;
import org.example.main.repository.StudentRepo;
import org.springframework.stereotype.Component;


@Component
public class IdGenerate
{
    private int instructor_id;
    private int student_id;

    public IdGenerate(InstructorRepo instructorRepo, StudentRepo studentRepo)
    {
        this.instructor_id = instructorRepo.maxInstructorId();
        this.student_id = studentRepo.maxStudentId();
    }

    public int getInstructor_id() {
        return ++instructor_id;
    }

    public int getStudent_id() {
        return ++student_id;
    }
}
