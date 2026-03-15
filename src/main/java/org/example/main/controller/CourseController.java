package org.example.main.controller;


import org.example.main.dto.CourseCountDTO;
import org.example.main.dto.CourseDTO;
import org.example.main.dto.CourseData;
import org.example.main.dto.CourseStudentDTO;
import org.example.main.entity.Course;
import org.example.main.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = {"http://localhost:3000","http://app.qa.company.com"})
public class CourseController
{
    CourseService courseService;
    public CourseController(CourseService courseService)
    {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public String addCourseData(@RequestBody CourseDTO coursedto)
    {
        return courseService.addCourse(coursedto);
    }

    @GetMapping("/get/{courseName}")
    public Course getCourseData(@PathVariable String courseName)
    {
        return courseService.findCourseById(courseName);
    }

    @GetMapping("/get-all")
    public Page<CourseStudentDTO> getCourses(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "courseFee") String value)  // for sorting pass columnName
    {
        return courseService.findAllCourses(page,size,value);
    }

    @PatchMapping("/update")
    public String updateCourseData(@RequestBody CourseDTO coursedto)
    {
        return courseService.updateCourse(coursedto);
    }

    @DeleteMapping("/delete/{id}")
    public String  deleteCourseData(@PathVariable String id)
    {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/get-all-courses")
    public List<CourseData> getStudentsWithSameCourse()
    {
        return courseService.studentsRegisterInSameCourse();
    }

    /*
    @GetMapping("/count-students-with-same-course")
    public List<CourseCountDTO> numberOfStudentsRegisterInSameCourse()
    {
      return courseService.numberOfStudentsRegisterInSameCourse();
    }
     */

    @GetMapping("/count/{id}")
    public Long getCourseCount(@PathVariable String id)
    {
        return courseService.getStudentCountByCourseId(id);
    }
}
