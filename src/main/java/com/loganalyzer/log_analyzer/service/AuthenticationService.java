package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.dto.LoginUserDto;
import com.loganalyzer.log_analyzer.dto.RegisterUserDto;
import com.loganalyzer.log_analyzer.model.User;
import com.loganalyzer.log_analyzer.repository.UserRepository;
import com.loganalyzer.log_analyzer.service.interfaces.AuthenticationServiceInterface;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(RegisterUserDto input){
        if(input.getEmail() == null || input.getEmail().isEmpty()){
            throw new IllegalArgumentException("Email is required");
        }
        User user = new User().setFullName(input.getName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));
        return userRepository.findByEmail(input.getEmail()).orElseThrow(()-> new RuntimeException("Unable to find the user..."));
    }
}
