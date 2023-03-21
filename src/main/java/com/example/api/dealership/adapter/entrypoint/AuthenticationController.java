package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;

import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.security.JwtService;
import com.example.api.dealership.adapter.service.user.UserService;
import de.mkammerer.argon2.Argon2Factory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RestController
@RequestMapping(path = "/v1/dealership")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Operation(summary = "Authenticate a valid user")
    @GetMapping(path = "/auths",produces = "application/json")
    public ResponseEntity<Response<String>> authenticate(@RequestBody UserDtoRequest userDtoRequest){

        var response = new Response<String>();
        try {
            var user = userMapper.toUserModel(userDtoRequest);

            var authenticatedUser = userService.auth(user);

            response.setData(jwtService.generateToken(authenticatedUser));

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

        if (optinalUser.isEmpty()) {
            var userModel = userMapper.toUserModel(userDtoRequest);

            userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

            var user = userService.saveUser(userModel);

            response.setData("User: " + user.getUsername() + " created with success");

            return ResponseEntity.ok().body(response);
        }
        response.setData("This username already exists.");

        return ResponseEntity.ok().body(response);
    }

}
