package com.example.api.dealership.adapter.service.user.impl;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryPort userRepositoryPort;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserModel saveUser(UserModel userModel) {
        if(userRepositoryPort.findByUsername(userModel.getUsername()).isPresent())
            throw new UsernameAlreadyUsedException("This username already exists");

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

        return userRepositoryPort.save(userModel);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepositoryPort.findByUsername(username);
    }
}
