package org.example.main.dto;

import lombok.Data;
import org.example.main.entity.Instructor;
import org.example.main.entity.Student;

import java.util.List;

@Data
public class CourseDTO
{
    public String courseId;
    public String courseName;
    public Double courseFee;
    public String modeType;
//    public List<Student> students;
    public Instructor instructor;
}
