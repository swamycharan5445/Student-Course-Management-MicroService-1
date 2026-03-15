package org.example.main.repository;

import org.example.main.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface StudentRepo extends JpaRepository<Student,Integer>,JpaSpecificationExecutor<Student>
{
     Student findByEmail(String email);

     @Query(value = "SELECT IFNULL(MAX(id), 0) FROM student", nativeQuery = true)
     int maxStudentId();

}
