package com.example.api.dealership.adapter.service.user.impl;

import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserModel saveUser(UserModel userModel) {
        return userRepositoryPort.save(userModel);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepositoryPort.findByUsername(username);
    }


    //TODO: Caso de errado, verificar a implementação da travels-java-api, porque lá ela n tem um método de auth dela.
    @Override
    public UserDetails auth(UserModel user) {
        var userDetails = loadUserByUsername(user.getUsername());
        if(passwordEncoder.matches(user.getPassword(), userDetails.getPassword()))
            return userDetails;

        throw new InvalidPasswordException("Invalid Password");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userModel = findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        var userRoles = getRoles(userModel);

        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .roles(userRoles.toString())
                .build();
    }

    private List<String> getRoles(UserModel userModel){
        return userModel.isAdmin() ? List.of("USER","ADMIN") : List.of("USER");
    }
}
