package org.example.main.feign;


import org.example.main.dto.StudentVerifyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "spring-task-one",url = "http://localhost:9999")
public interface SpringFeignClient
{
    @GetMapping("/students/verify/{id}")
    public StudentVerifyDTO studentVerify(@PathVariable Integer id);
}
