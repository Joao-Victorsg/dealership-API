package com.example.api.dealership.adapter.service.user;

import com.example.api.dealership.core.domain.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserModel saveUser(UserModel userModel);

    Optional<UserModel> findByUsername(String username);

    UserDetails auth(UserModel user);


}
