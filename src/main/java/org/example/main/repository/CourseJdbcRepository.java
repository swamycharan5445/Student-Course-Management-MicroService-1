package org.example.main.repository;


import com.zaxxer.hikari.HikariDataSource;
import org.example.main.dto.CourseCountDTO;
import org.example.main.dto.CourseStudentDTO;
import org.example.main.entity.Course;
import org.example.main.entity.Instructor;
import org.example.main.entity.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CourseJdbcRepository
{

    private final JdbcTemplate jdbcTemplate;

    public CourseJdbcRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Course> courseRowMapper = new RowMapper<Course>() {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setCourseId(rs.getString("course_id"));
            course.setCourseName(rs.getString("course_name"));
            course.setCourseFee(rs.getDouble("course_fee"));
            course.setModeType(rs.getString("mode_type"));
            return course;
        }
    };

    private final RowMapper<Instructor> instructorRowMapper=new  RowMapper<Instructor>() {
        @Override
        public Instructor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Instructor instructor = new Instructor();
            instructor.setId(rs.getInt("id"));
            instructor.setName(rs.getString("name"));
            return instructor;
        }
    };
    private final RowMapper<Student> studentRowMapper=new   RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setEmail(rs.getString("email"));
            student.setAge(rs.getInt("age"));
            return student;
        }
    };

    public List<Course> getCoursesWithStudents()
    {
        String sql = " SELECT DISTINCT c.*  FROM course c  LEFT JOIN student_course cs ON c.course_id = cs.course_id LEFT JOIN student s ON s.id = cs.student_id";
        return jdbcTemplate.query(sql, courseRowMapper);

    }


    public List<CourseCountDTO> getCourseWithStudentCount() {
        String sql = "SELECT c.course_id, COUNT(s.id) AS student_count FROM course c LEFT JOIN student_course cs ON c.course_id = cs.course_id LEFT JOIN student s ON s.id = cs.student_id GROUP BY c.course_id";

        return jdbcTemplate.query(sql, new RowMapper<CourseCountDTO>() {
            @Override
            public CourseCountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                String courseId = rs.getString("course_id");
                Long studentCount = rs.getLong("student_count");
                return new CourseCountDTO(courseId, studentCount);
            }
        });
    }


    public Course findByCourseId(String courseId)
    {
        String sql = "SELECT * FROM course WHERE course_id = ?";
        Course course = jdbcTemplate.queryForObject(sql, courseRowMapper, courseId);

        if (course != null)
        {
            course.setInstructor(findInstructorByCourseId(courseId));
            course.setStudents(findStudentsByCourseId(courseId));
        }
        return course;
    }


    public int deleteByCourseId(String courseId)
    {
        String sql = "DELETE FROM course WHERE course_id = ?";
        return jdbcTemplate.update(sql, courseId);
    }

    public Instructor findInstructorByCourseId(String courseId)
    {
        String sql = " SELECT i.*  FROM instructor i JOIN course c ON c.instructor_id = i.id WHERE c.course_id = ?";

        return jdbcTemplate.queryForObject(sql, instructorRowMapper, courseId);
    }

    public List<Student> findStudentsByCourseId(String courseId)
    {
        String sql = " SELECT s.* FROM student s JOIN student_course cs ON cs.student_id = s.id WHERE cs.course_id = ?";

        return jdbcTemplate.query(sql, studentRowMapper, courseId);
    }


    public List<CourseStudentDTO> findAllCourses(int page, int size) {

        int offset = page * size;

        String sql = """
        SELECT
            c.course_id,
            c.course_name,
            c.course_fee,
            c.mode_type,
            s.id AS student_id,
            s.name AS student_name,
            s.email AS student_email,
            s.age AS student_age
        FROM course c
        LEFT JOIN student_course sc ON c.course_id = sc.course_id
        LEFT JOIN student s ON sc.student_id = s.id
        ORDER BY c.course_fee DESC
        LIMIT ? OFFSET ?
    """;

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(sql, size, offset);

        Map<String, Course> courseMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {

            String courseId = (String) row.get("course_id");

            Course course = courseMap.get(courseId);
            if (course == null) {

                course = new Course();
                course.setCourseId(courseId);
                course.setCourseName((String) row.get("course_name"));
                course.setCourseFee((Double) row.get("course_fee"));
                course.setModeType((String) row.get("mode_type"));

                /*Integer instructorId = (Integer) row.get("instructor_id");
                if (instructorId != null) {
                    Instructor instructor = new Instructor();
                    instructor.setId(instructorId);
                    instructor.setName((String) row.get("instructor_name"));

                    course.setInstructor(instructor);
                }*/

                course.setStudents(new ArrayList<>());
                courseMap.put(courseId, course);
            }

            Integer studentId = (Integer) row.get("student_id");
            if (studentId != null) {
                Student student = new Student();
                student.setId(studentId);
                student.setName((String) row.get("student_name"));
                student.setEmail((String) row.get("student_email"));
                student.setAge((Integer) row.get("student_age"));

                course.getStudents().add(student);
            }
        }
        List<CourseStudentDTO> result = new ArrayList<>();

        for (Course course : courseMap.values()) {
            CourseStudentDTO dto = new CourseStudentDTO();
            dto.setCourseId(course.getCourseId());
            dto.setCourseName(course.getCourseName());
            dto.setCourseFee(course.getCourseFee());
            dto.setModeType(course.getModeType());
            dto.setStudents(course.getStudents());

            result.add(dto);
        }

        return result;

//        return new ArrayList<>(courseMap.values());
    }


    public int countCourses() {
        String sql = "SELECT COUNT(*) FROM course";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }




}
