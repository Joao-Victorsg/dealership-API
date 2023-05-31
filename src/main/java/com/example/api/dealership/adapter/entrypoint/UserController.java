package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(path = "/v1/dealership")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Operation(summary = "Create a user")
    @PostMapping(path = "/users",produces = "application/json")
    public ResponseEntity<Response<String>> saveUser(@RequestBody UserDtoRequest userDtoRequest){

        var response = new Response<String>();

        var optinalUser = userService.findByUsername(userDtoRequest.getUsername());

        if (optinalUser.isPresent()) {
            response.setData("This username already exists.");

            return ResponseEntity.badRequest().body(response);
        }

        var userModel = userMapper.toUserModel(userDtoRequest);

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

        var user = userService.saveUser(userModel);

        response.setData("User: " + user.getUsername() + " created with success");

        return ResponseEntity.created(URI.create("/v1/dealership/users/")).body(response);
    }

}
