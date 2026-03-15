package org.example.main.controller;

import feign.FeignException;
import org.example.main.dto.CourseDTO;
import org.example.main.dto.CourseData;
import org.example.main.dto.StudentDTO;
import org.example.main.dto.StudentVerifyDTO;
import org.example.main.entity.Course;
import org.example.main.entity.Student;
import org.example.main.exception.StudentNotFoundException;
import org.example.main.feign.SpringFeignClient;
import org.example.main.proxy.CourseRecommendationProxy;
import org.example.main.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = {"http://localhost:3000","http://app.qa.company.com"})
public class StudentController
{

    private final StudentService studentService;
    private final SpringFeignClient springFeignClient;
    private final CourseRecommendationProxy courseRecommendationProxy;

    public StudentController(StudentService studentService, SpringFeignClient springFeignClient, CourseRecommendationProxy courseRecommendationProxy)
    {
        this.studentService = studentService;
        this.springFeignClient = springFeignClient;
        this.courseRecommendationProxy = courseRecommendationProxy;
    }

    @PostMapping("/add")
    public String addStudentData(@RequestBody StudentDTO studentdto)
    {
        return studentService.addStudent(studentdto);
    }

    @GetMapping("/get/{email}")
    public Student getStudentData(@PathVariable String email)
    {

        return studentService.getStudentByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public List<Student> getAllStudents(@RequestParam(defaultValue = "0")int page,
                                        @RequestParam(defaultValue = "10")int size,
                                        @RequestParam(defaultValue = "name")String sorting)
    {
        return studentService.getAllStudents(page,size,sorting);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update")
    public String updateStudentData(@RequestBody StudentDTO studentdto)
    {
        return studentService.updateStudent(studentdto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteStudentData(@PathVariable Integer id)
    {
        return studentService.deleteStudent(id);
    }

    @PutMapping("/enroll/{student-id}/course/{course-id}")
    public String enrollStudent(@PathVariable(name = "student-id") Integer student_id,
                                @PathVariable(name = "course-id") String course_id)
    {
        return studentService.enrollStudentIntoCourse(student_id,course_id);
    }

    @PutMapping("/remove/{student-id}/course/{course-id}")
    public String removeStudentFromCourse(@PathVariable(name = "student-id") Integer student_id,
                                          @PathVariable(name ="course-id" ) String course_id)
    {
        return studentService.removeStudentFromCourse(student_id,course_id);
    }

//    @GetMapping("/filter/name")
//    public Page<Student> filterByName(@RequestParam String name,
//                                      @RequestParam(defaultValue = "0") int page,
//                                      @RequestParam(defaultValue = "10") int size)
//    {
//        return studentService.filterByName(name, page, size);
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public Page<Student> filterByAge(@RequestParam String name,
                                     @RequestParam Integer minAge,
                                     @RequestParam Integer maxAge,
                                     @RequestParam String courseName,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size)
    {
        return studentService.filterByAll(name,minAge, maxAge,courseName, page, size);
    }

//    @GetMapping("/filter/coursename")
//    public Page<Student> filterByCourse(@RequestParam String courseName,
//                                        @RequestParam(defaultValue = "0") int page,
//                                        @RequestParam(defaultValue = "10") int size)
//    {
//        return studentService.filterByCourse(courseName, page, size);
//    }

    @GetMapping(value = "/course-recommend/{id}")
    public Mono<List<String>> courseRecommendation(@PathVariable Integer id)
    {
         return courseRecommendationProxy.recommend(id);
    }

    @GetMapping("/identity-verification/{id}")
    public StudentVerifyDTO identityVerification(@PathVariable Integer id)
    {
        return springFeignClient.studentVerify(id);
    }

}

