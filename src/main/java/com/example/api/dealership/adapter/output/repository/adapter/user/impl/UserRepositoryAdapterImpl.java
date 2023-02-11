package com.example.api.dealership.adapter.output.repository.adapter.user.impl;

import com.example.api.dealership.adapter.output.repository.adapter.user.UserRepositoryAdapter;
import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.core.domain.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserRepositoryAdapterImpl implements UserRepositoryAdapter {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserModel saveUser(UserModel userModel) {
        return userRepositoryPort.save(userModel);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepositoryPort.findByUsername(username);
    }

}
