package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.dto.LoginUserDto;
import com.loganalyzer.log_analyzer.dto.RegisterUserDto;
import com.loganalyzer.log_analyzer.exceptions.FileProcessingException;
import com.loganalyzer.log_analyzer.model.User;
import com.loganalyzer.log_analyzer.repository.UserRepository;
import com.loganalyzer.log_analyzer.service.interfaces.AuthenticationServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        log.info("Attempting to register user with email: {}", input.getEmail());
        if(input.getEmail() == null || input.getEmail().isEmpty()){
            log.error("User registration failed: Email is required.");
            throw new FileProcessingException("Email is required for registration.");
        }
        try{
            User user = new User().setFullName(input.getName())
                    .setEmail(input.getEmail())
                    .setPassword(passwordEncoder.encode(input.getPassword()));
            User savedUser = userRepository.save(user);
            log.info("User registered successfully with email: {}", input.getEmail());
            return savedUser;
        }catch (Exception e){
            log.error("Error during user registration: {}", e.getMessage(), e);
            throw new FileProcessingException("User registration failed.", e);
        }
    }

    public User authenticate(LoginUserDto input){
        log.info("Authenticating user with email: {}", input.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));
            return userRepository.findByEmail(input.getEmail())
                    .orElseThrow(()->{
                        log.error("User authentication failed: Unable to find user.");
                        return new FileProcessingException("User not found.");
                    });
        }catch (Exception e){
            log.error("Error during authentication for email {}: {}", input.getEmail(), e.getMessage(), e);
            throw new FileProcessingException("Authentication failed.", e);
        }
    }
}
