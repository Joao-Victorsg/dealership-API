package com.example.api.dealership.adapter.service.user;

import com.example.api.dealership.core.domain.UserModel;

import java.util.Optional;


public interface UserService{
    UserModel saveUser(UserModel userModel);

    Optional<UserModel> findByUsername(String username);
}
