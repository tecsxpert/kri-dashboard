package com.internship.tool.service;

import com.internship.tool.dto.AuthRequest;
import com.internship.tool.dto.AuthResponse;
import com.internship.tool.dto.RegisterRequest;

/**
 * Service interface for user authentication operations.
 */
public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}
