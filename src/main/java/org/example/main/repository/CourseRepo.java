package org.example.main.repository;

import org.example.main.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course,String>
{
    Course findByCourseId(String courseId);

    void deleteByCourseId(String courseId);
    Course findByCourseName(String courseName);

/*
     @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
     List<Course> getCoursesWithStudents();

 */

     @Query(value = "SELECT DISTINCT c.* FROM course c LEFT JOIN student_course cs ON c.course_id = cs.course_id LEFT JOIN student s ON s.id = cs.student_id",nativeQuery = true)
     List<Course> getCoursesWithStudents();



/*
     @Query("SELECT c.courseId, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.courseId")
     List<Object[]> getCourseWithStudentCount();

 */
/*
    @Query(value = "SELECT c.course_id, COUNT(s.id) FROM course c LEFT JOIN student_course cs ON c.course_id = cs.course_id LEFT JOIN student s ON s.id = cs.student_id GROUP BY c.course_id", nativeQuery = true)
    List<Object[]> getCourseWithStudentCount();
 */
    @Query(value = "SELECT COUNT(s.id) FROM course c LEFT JOIN student_course cs ON c.course_id = cs.course_id LEFT JOIN student s ON s.id = cs.student_id WHERE c.course_id = :courseId",nativeQuery = true)
     Long getStudentCountByCourseId(@Param("courseId") String courseId);


}
