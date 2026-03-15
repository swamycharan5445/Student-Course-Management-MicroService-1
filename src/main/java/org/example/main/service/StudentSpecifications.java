package org.example.main.service;



import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.example.main.entity.Student;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecifications {

    public static Specification<Student> filterAll(String name,Integer minAge,Integer maxAge,String courseName)
    {
        return (root, criteriaQuery, criteriaBuilder) -> {


            List<Predicate> predicates = new ArrayList<>();
            if(name!=null && !name.isEmpty())
            {
                predicates.add(criteriaBuilder.equal(root.get("name"),name));
            }
            if(minAge!=null)
            {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"),minAge));
            }
            if(maxAge!=null)
            {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"),maxAge));
            }
            if (courseName != null && !courseName.isEmpty())
            {
                Join<Object, Object> courses = root.join("courses");
                predicates.add(criteriaBuilder.like(courses.get("courseName"), "%" + courseName + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

//    public static Specification<Student> minAge(Integer minAge)
//    {
//        return (root, query, cb) ->
//                minAge == null ? null : cb.greaterThanOrEqualTo(root.get("age"), minAge);
//    }
//
//    public static Specification<Student> maxAge(Integer maxAge)
//    {
//        return (root, query, cb) ->
//                maxAge == null ? null : cb.lessThanOrEqualTo(root.get("age"), maxAge);
//    }
//
//    public static Specification<Student> course(String courseName)
//    {
//        return (root, query, cb) -> {
//            if (courseName == null) return null;
//            Join<Object, Object> courseJoin = root.join("courses");
//            return cb.like(courseJoin.get("courseName"), "%" + courseName + "%");
//        };
//    }
}
