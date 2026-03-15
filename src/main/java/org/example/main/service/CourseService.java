package org.example.main.service;

import jakarta.transaction.Transactional;
import org.example.main.dto.CourseCountDTO;
import org.example.main.dto.CourseDTO;
import org.example.main.dto.CourseData;
import org.example.main.dto.CourseStudentDTO;
import org.example.main.entity.Course;
import org.example.main.entity.Instructor;
import org.example.main.exception.CourseNotFoundException;
import org.example.main.exception.InstructorNotFoundException;
import org.example.main.repository.CourseJdbcRepository;
import org.example.main.repository.CourseRepo;
import org.example.main.repository.InstructorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService
{
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    CourseRepo courseRepo;
    InstructorRepo instructorRepo;
    CourseJdbcRepository courseJdbcRepository;
    public CourseService(CourseRepo courseRepo, InstructorRepo instructorRepo, CourseJdbcRepository courseJdbcRepository)
    {
        this.courseRepo = courseRepo;
        this.instructorRepo = instructorRepo;
        this.courseJdbcRepository = courseJdbcRepository;
    }

    public String addCourse(CourseDTO coursedto)
    {
        Course byCourseId = courseRepo.findByCourseId(coursedto.getCourseId());
        if(byCourseId != null)
        {
            return coursedto.getCourseId()+" already exists";
        }

        if(coursedto.getInstructor()!=null )
        {
            Course course = new Course();
            course.setCourseName(coursedto.getCourseName());
            course.setCourseId(coursedto.getCourseId());
            course.setCourseFee(coursedto.getCourseFee());
            course.setModeType(coursedto.getModeType());
//        course.setStudents(coursedto.getStudents());
            course.setInstructor(coursedto.getInstructor());
            courseRepo.save(course);
            String message = "Course added successfully";
            log.info(message);
            return message;
        }
            log.error("Instructor not found");
            throw new InstructorNotFoundException("Instructor not found");


    }
    public Course findCourseById(String courseName)
    {
        Course byCourseName = courseRepo.findByCourseName(courseName);
//        Course courseById = courseRepo.findByCourseId(id);
//        Course courseById = courseJdbcRepository.findByCourseId(id);
        if(byCourseName != null)
        {
            String msg = "Course found with name: "+courseName;
            log.info(msg);
            return byCourseName;
        }
        else
        {
            String msg = "Course not found with name: "+courseName;
            log.error(msg);
            throw new CourseNotFoundException(msg);
        }
    }

    public String updateCourse(CourseDTO coursedto)
    {
//        Course existing = courseRepo.findByCourseId(coursedto.getCourseId());
        Course existing = courseJdbcRepository.findByCourseId(coursedto.getCourseName());
//        Instructor instructor = instructorRepo.findById(coursedto.getInstructor().getId()).orElseThrow(() -> new InstructorNotFoundException("Instructor not found with id " + coursedto.getInstructor().getId()));
        if(existing != null)
        {
            if(coursedto.getCourseName() != null)
                existing.setCourseName(coursedto.getCourseName());
            if(coursedto.getCourseFee()!= null)
                existing.setCourseFee(coursedto.getCourseFee());
            if(coursedto.getModeType() != null)
                existing.setModeType(coursedto.getModeType());
            if(coursedto.getInstructor() != null)
                existing.setInstructor(coursedto.getInstructor());
            courseRepo.save(existing);
            String message = "Course updated successfully";
            log.info(message);
            return message;

        }
        else
        {
            String message = "Course not found with id: "+coursedto.getCourseName();
            log.error(message);
            return message;
        }
    }


    public Page<CourseStudentDTO> findAllCourses(int page, int size, String value)
    {
        List<CourseStudentDTO> courses = courseJdbcRepository.findAllCourses(page, size);
//        Page<Course> courses = courseRepo.findAll(PageRequest.of(0, 10, Sort.by(value).descending()));
        if(courses.isEmpty())
        {
            throw new CourseNotFoundException("Course not found");
        }
        log.info("Courses found");

        return new PageImpl<>(courses);
    }

    @Transactional
    public String deleteCourse(String id)
    {
        Course c = courseRepo.findByCourseId(id);

        if (c == null)
            return "Course not found";
        log.info("Course deleted successfully");
        courseRepo.deleteByCourseId(id);
       /* if(true)
        {
            throw new CourseNotFoundException("Testing transaction");
        }*/
        return "Course deleted successfully";
    }

    public List<CourseData> studentsRegisterInSameCourse()
    {
//        return courseRepo.getCoursesWithStudents();
        List<Course> coursesWithStudents = courseJdbcRepository.getCoursesWithStudents();
        List<CourseData> courseDataList = new ArrayList<>();
        for(Course course : coursesWithStudents)
        {
            CourseData courseData = new CourseData();

            courseData.setCourseId(course.getCourseId());
            courseData.setCourseName(course.getCourseName());
            courseData.setCourseFee(course.getCourseFee());
            courseData.setModeType(course.getModeType());
            courseDataList.add(courseData);
        }
        log.info("Courses with students found");
        return courseDataList;


    }
/*
    public List<CourseCountDTO> numberOfStudentsRegisterInSameCourse()
    {
        return courseJdbcRepository.getCourseWithStudentCount();
//        List<Object[]> courseWithStudentCount = courseRepo.getCourseWithStudentCount();
//        List<CourseCountDTO> list = new ArrayList<>();
//
//        for(Object[] objects : courseWithStudentCount)
//        {
//            String courseId = objects[0].toString();
//            long studentCount  = ((Number) objects[1]).longValue();
//            list.add(new CourseCountDTO(courseId,studentCount));
//        }
//        return list;
    }

 */
    public Long getStudentCountByCourseId(String id)
    {
       return courseRepo.getStudentCountByCourseId(id);
    }
    
    
    
    
}
