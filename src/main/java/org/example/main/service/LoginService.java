package org.example.main.service;

import org.example.main.entity.UserData;
import org.example.main.repository.UserDataRepo;
import org.example.main.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class LoginService
{
    private final UserDataRepo userDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public LoginService(UserDataRepo userDataRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil)
    {
        this.userDataRepo = userDataRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String userRegister(UserData userData)
    {
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        userDataRepo.save(userData);
        return "User Login Success";
    }


    public String userLogin(String username, String password)
    {
        Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password));

//        SecurityContextHolder.getContext().setAuthentication(authentication);
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       System.err.println(userDetails.getAuthorities());
        Optional<UserData> byUsername = userDataRepo.findByUsername(username);
        return jwtUtil.generateToken(byUsername.get());
    }
}
