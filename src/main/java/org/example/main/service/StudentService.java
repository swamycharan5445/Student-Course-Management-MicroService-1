package org.example.main.service;

import jakarta.transaction.Transactional;
import org.example.main.configs.ExternalCommunication;
import org.example.main.dto.CourseDTO;
import org.example.main.dto.CourseData;
import org.example.main.dto.StudentDTO;
import org.example.main.entity.Course;
import org.example.main.entity.Student;
import org.example.main.entity.UserData;
import org.example.main.exception.CourseNotFoundException;
import org.example.main.exception.StudentNotFoundException;
import org.example.main.repository.CourseRepo;
import org.example.main.repository.StudentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class StudentService {

    private Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final IdGenerate idGenerate;
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final ExternalCommunication externalCommunication;
    private final CustomUserDetailService customUserDetailService;

    public StudentService(StudentRepo studentRepo, CourseRepo courseRepo, IdGenerate idGenerate, WebClient webClient, RestTemplate restTemplate, ExternalCommunication externalCommunication, CustomUserDetailService customUserDetailService)
    {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.idGenerate = idGenerate;
        this.webClient = webClient;
        this.restTemplate = restTemplate;
        this.externalCommunication = externalCommunication;
        this.customUserDetailService = customUserDetailService;
    }

    @Transactional
    public String addStudent(StudentDTO studentdto)
    {
        Student existing = studentRepo.findByEmail(studentdto.getEmail());

        if (existing != null)
            return "Student already exists with email: ";

        Student student = new Student();
        student.setName(studentdto.getName());
        student.setEmail(studentdto.getEmail());
        student.setAge(studentdto.getAge());
        student.setId(idGenerate.getStudent_id());

        studentRepo.save(student);
        String message="Added student with email: " + studentdto.getEmail();
        log.info(message);
        return "Student added successfully";
    }

    public Student getStudentByEmail(String email)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserData principal = (UserData) auth.getPrincipal();
        String authEmail = principal.getEmail();
        Student byEmail = studentRepo.findByEmail(email);

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser"))
        {
            throw new AccessDeniedException("Unauthorized");
        }

        if(byEmail == null)
        {
            throw new StudentNotFoundException("Student not found with email: " + email);
        }
        if(!byEmail.getEmail().equals(authEmail))
        {
            throw new AccessDeniedException("You are not allowed to access this student");
        }
        return byEmail;
//        return studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public String updateStudent(StudentDTO studentdto)  {
        Student existing = studentRepo.findById(studentdto.getId()).orElseThrow(() -> new StudentNotFoundException("Student not found"));

        if (studentdto.getEmail() != null)
            existing.setEmail(studentdto.getEmail());

        if (studentdto.getName() != null)
            existing.setName(studentdto.getName());

        if (studentdto.getAge() != null)
            existing.setAge(studentdto.getAge());

        studentRepo.save(existing);
        log.info(existing.getName() + " updated successfully");
        return "Student updated successfully";

    }

    public String deleteStudent(Integer id)  {
        Student existing = studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));

        studentRepo.delete(existing);
        log.info(existing.getName() + " deleted successfully");
        return "Student deleted successfully";
    }

    public List<Student> getAllStudents(int page, int size, String sorting)
    {
        PageRequest name = PageRequest.of(0, 5, Sort.by(sorting));
        List<Student> students = studentRepo.findAll(name).getContent();
        if (students.isEmpty())
            throw new StudentNotFoundException("No students found");
        log.info("Found {} students", students.size());
        return students;
    }

    @Transactional
    public String enrollStudentIntoCourse(Integer student_id, String course_id)
    {


        Student byId = studentRepo.findById(student_id).orElseThrow(()->new StudentNotFoundException("Student not found"));
        Course byCourseId = courseRepo.findByCourseId(course_id);
        if(byCourseId == null)
            throw new CourseNotFoundException("Course not found with id " + course_id);
        List<Course> courses = byId.getCourses();
        if(!courses.contains(byCourseId))
            courses.add(byCourseId);
        else
            return "Student-Id : "+student_id+" already enrolled in course-Id : "+course_id;
        byId.setCourses(courses);
        studentRepo.save(byId);

       /* ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:9999/send/mail/{id}"
                                                                , HttpMethod.GET
                                                                , null
                                                                , String.class, student_id);*/

        String s = externalCommunication.StudentEnroll(student_id);

        log.info(s);
        return "Student enroll successfully \n"+s;

    }

    @Transactional
    public String removeStudentFromCourse(Integer studentId, String courseId)
    {
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found"));
        Course byCourseId = courseRepo.findByCourseId(courseId);
        if(byCourseId == null)
            throw new CourseNotFoundException("Course not found with id " + courseId);
        List<Course> courses =student.getCourses();
        courses.remove(byCourseId);
        student.setCourses(courses);
        studentRepo.save(student);
        log.info(student.getName() + " removed successfully ");
        return "Student removed successfully";
    }

//    public Page<Student> filterByName(String name, int page, int size)
//    {
//        Specification<Student> spec = StudentSpecifications.name(name);
//        return studentRepo.findAll(spec, PageRequest.of(page, size));
//    }

    public Page<Student> filterByAll(String name,Integer minAge, Integer maxAge,String courseName, int page, int size)
    {
        Specification<Student> spec = Specification.where(StudentSpecifications.filterAll(name, minAge, maxAge, courseName));
        return studentRepo.findAll(spec, PageRequest.of(page, size));
    }

//    public Page<Student> filterByCourse(String courseName, int page, int size)
//    {
//        Specification<Student> spec = StudentSpecifications.course(courseName);
//        return studentRepo.findAll(spec, PageRequest.of(page, size));
//    }

    public Flux<String> courseRecommendation(Integer id)
    {
        Flux<String> stringFlux = webClient.get().uri("http://localhost:9999/recommend/{id}", id)
                                  .accept(MediaType.APPLICATION_JSON)
                                  .retrieve()
                                  .bodyToFlux(String.class);
        log.info("stringFlux : {}", stringFlux);
        return stringFlux;

    }

}

