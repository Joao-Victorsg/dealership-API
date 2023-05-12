package com.example.api.dealership.adapter.service.security.impl;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.core.domain.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userModel = userRepositoryPort.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .roles(getRoles(userModel))
                .build();
    }

    private String getRoles(UserModel userModel){
        return userModel.isAdmin() ? "ADMIN" : "USER";
    }

}
