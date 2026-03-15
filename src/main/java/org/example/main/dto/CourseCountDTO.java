package org.example.main.dto;


import lombok.Getter;

@Getter
public class CourseCountDTO
{
    private final String courseId;
    private final Long studentCount;

    public CourseCountDTO(String courseId, Long studentCount) {
        this.courseId = courseId;
        this.studentCount = studentCount;
    }
}

