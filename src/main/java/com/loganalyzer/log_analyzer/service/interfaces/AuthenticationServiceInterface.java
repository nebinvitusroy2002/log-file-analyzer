package com.loganalyzer.log_analyzer.service.interfaces;

import com.loganalyzer.log_analyzer.dto.LoginUserDto;
import com.loganalyzer.log_analyzer.dto.RegisterUserDto;
import com.loganalyzer.log_analyzer.model.User;

public interface AuthenticationServiceInterface {
    public User signUp(RegisterUserDto input);
    public User authenticate(LoginUserDto input);
}
