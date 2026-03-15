package org.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableFeignClients
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class SpringTaskApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringTaskApplication.class, args);
    }
}
