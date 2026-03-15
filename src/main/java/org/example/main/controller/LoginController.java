package org.example.main.controller;

import org.example.main.entity.UserData;
import org.example.main.service.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController
{
    private LoginService loginService;
    public LoginController(LoginService loginService)
    {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public String userRegister(@RequestBody UserData userData)
    {
       return loginService.userRegister(userData);
    }

    @GetMapping("/login")
    public String userLogin(@RequestParam String username, @RequestParam String password)
    {
        return loginService.userLogin(username, password);
    }

}
