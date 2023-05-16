package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;

import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.security.AuthenticationService;
import com.example.api.dealership.adapter.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/v1/dealership")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    private final UserMapper userMapper;

    @Operation(summary = "Authenticate a valid user")
    @PostMapping(path = "/auths",produces = "application/json")
    public ResponseEntity<Response<String>> authenticate(@RequestBody UserDtoRequest userDtoRequest){

        var response = new Response<String>();
        try {
            final var token = authenticationService.authenticate(userDtoRequest);
            response.setData(token);

            return ResponseEntity.ok().body(response);
        }catch (Exception ex){
            response.setErrors(ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

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

        return ResponseEntity.ok().body(response);
    }

}
