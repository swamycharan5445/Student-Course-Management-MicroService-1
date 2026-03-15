package org.example.main.dto;

import lombok.Data;

import java.util.Set;

@Data
public class InstructorDTO
{
    public Integer id;
    public String name;
    public Set<String> expertise;
}
