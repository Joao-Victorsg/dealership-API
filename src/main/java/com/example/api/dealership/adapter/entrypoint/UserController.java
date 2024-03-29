package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
//@Profile("!prod")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the user"),
            @ApiResponse(responseCode = "500", description = "There was internal server error"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/users",produces = "application/json")
    public ResponseEntity<Response<String>> saveUser(@RequestBody UserDtoRequest userDtoRequest) throws UsernameAlreadyUsedException {

        log.info("Creating user");

        final var user = userService.saveUser(userMapper.toUserModel(userDtoRequest));

        log.info("Created user:",user);

        final var response  = Response.createResponse("User: " + user.getUsername() + " created with success");

        return ResponseEntity.created(URI.create("/v1/dealership/users/")).body(response);
    }

}
