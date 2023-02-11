package com.example.api.dealership.adapter.output.repository.adapter.user;

import com.example.api.dealership.core.domain.UserModel;

import java.util.Optional;

public interface UserRepositoryAdapter {
    UserModel saveUser(UserModel userModel);

    Optional<UserModel> findByUsername(String username);
}
