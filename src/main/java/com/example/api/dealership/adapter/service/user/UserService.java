package com.example.api.dealership.adapter.service.user;

import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;

import java.util.Optional;


public interface UserService{
    UserModel saveUser(UserModel userModel) throws UsernameAlreadyUsedException;

    Optional<UserModel> findByUsername(String username);
}
