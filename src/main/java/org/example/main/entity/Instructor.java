package org.example.main.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Instructor
{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String name;
    @ElementCollection
    public Set<String> expertise;

//    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
//    @JsonIgnore
//    public List<Course> courses;

}
