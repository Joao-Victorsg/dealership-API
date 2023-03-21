package com.example.api.dealership.adapter.service.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userModel);

    boolean isTokenValid(String token);

    String getUsername(String token);

}
