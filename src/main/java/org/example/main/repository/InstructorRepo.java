package org.example.main.repository;

import org.example.main.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstructorRepo extends JpaRepository<Instructor,Integer>
{
    @Query(value = "SELECT IFNULL(MAX(id), 0) FROM instructor", nativeQuery = true)
    int maxInstructorId();

    Instructor findByName(String name);

}
