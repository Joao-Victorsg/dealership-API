package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.security.AuthenticationService;
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

@Slf4j
@RestController
@RequestMapping(path = "/v1/dealership")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate a valid user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized action"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the user"),
            @ApiResponse(responseCode = "500", description = "There was internal server error"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/auths",produces = "application/json")
    public ResponseEntity<Response<String>> authenticate(@RequestBody UserDtoRequest userDtoRequest){
            final var token = authenticationService.authenticate(userDtoRequest);
            final var response = Response.createResponse(token);

            return ResponseEntity.ok().body(response);
    }



}

