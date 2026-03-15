package org.example.main.dto;


import lombok.Data;
import org.example.main.entity.Course;

import java.util.List;

@Data
public class StudentDTO
{
    public Integer id;
    public String name;
    public String email;
    public Integer age;
//    public List<Course> courses;

}
