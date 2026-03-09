package edu.psy.service;

import edu.psy.dto.LoginRequest;
import edu.psy.dto.LoginResponse;
import edu.psy.dto.RegisterRequest;
import edu.psy.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
