/*
package com.example.api.dealership.adapter.service.security.impl;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.security.AuthenticationService;
import com.example.api.dealership.adapter.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    public String authenticate(UserDtoRequest userDtoRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDtoRequest.getUsername(),userDtoRequest.getPassword()));

        final var userDetails = userDetailsService.loadUserByUsername(userDtoRequest.getUsername());

        return jwtService.generateToken(userDetails);
    }
}
*/
