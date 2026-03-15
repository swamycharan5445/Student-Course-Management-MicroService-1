package org.example.main.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String courseId;
    public String courseName;
    public Double courseFee;
    public String modeType;

//    @JsonIgnore
    @ManyToMany(mappedBy = "courses")
    public List<Student> students;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    public Instructor instructor;
}
