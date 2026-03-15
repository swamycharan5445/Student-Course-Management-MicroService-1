package org.example.main.controller;


import org.example.main.dto.InstructorDTO;
import org.example.main.entity.Instructor;
import org.example.main.exception.InstructorNotFoundException;
import org.example.main.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructor")
@CrossOrigin(origins = {"http://localhost:3000","http://app.qa.company.com"})
public class InstructorController
{
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService)
    {
        this.instructorService = instructorService;
    }

    @PostMapping("/add")
    public String addInstructorData(@RequestBody InstructorDTO instructordto)
    {
        return instructorService.addInstructor(instructordto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{name}")
    public Instructor getInstructorData(@PathVariable String name)
    {
        return instructorService.findInstructorById(name);
    }

    @GetMapping("/get-all")
    public List<Instructor> getAllInstructors()
    {
        return instructorService.findAllInstructors();
    }

    @DeleteMapping("/delete/{name}")
    public String deleteInstructorData(@PathVariable String name)
    {
        return instructorService.deleteInstructor(name);
    }
}

