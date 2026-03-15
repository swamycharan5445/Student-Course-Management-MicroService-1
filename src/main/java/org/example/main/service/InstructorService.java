package org.example.main.service;

import org.example.main.dto.InstructorDTO;
import org.example.main.entity.Instructor;
import org.example.main.exception.InstructorNotFoundException;
import org.example.main.repository.InstructorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InstructorService
{

    private static final Logger log = LoggerFactory.getLogger(InstructorService.class);
    private final InstructorRepo instructorRepo;
    private final IdGenerate idGenerate;

    public InstructorService(InstructorRepo instructorRepo, IdGenerate idGenerate)
    {
        this.instructorRepo = instructorRepo;
        this.idGenerate = idGenerate;
    }

    public String addInstructor(InstructorDTO instructordto)
    {
        Instructor instructor = new Instructor();
        instructor.setName(instructordto.getName());
        instructor.setId(idGenerate.getInstructor_id());
        instructor.setExpertise(instructordto.getExpertise());
        instructorRepo.save(instructor);
        String msg = "Instructor added successfully!";
        log.info(msg);
        return msg;
    }

    public Instructor findInstructorById(String name)
    {
        log.info("Instructor finding instructor by name {}", name);
        Instructor byName = instructorRepo.findByName(name);
        if(byName == null)
            throw new InstructorNotFoundException("Instructor not found");
        return byName;
//        return instructorRepo.findById(id)
//                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id " + id));
    }

    public List<Instructor> findAllInstructors()
    {
        List<Instructor> list = instructorRepo.findAll();
        if (list.isEmpty())
            throw new RuntimeException("No instructors found");
        log.info("Instructors found");
        return list;
    }

    public String deleteInstructor(String name)
    {
//        Instructor instructor = instructorRepo.findById(id)
//                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id " + id));
        Instructor byName = instructorRepo.findByName(name);
        if(byName == null)
            throw new InstructorNotFoundException("Instructor not found");
        instructorRepo.delete(byName);
        String msg = "Instructor deleted successfully!";
        log.info(msg);
        return msg;
    }
}

