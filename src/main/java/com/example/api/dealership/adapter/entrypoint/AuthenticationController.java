package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDto;
import com.example.api.dealership.adapter.output.repository.adapter.user.UserRepositoryAdapter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepositoryAdapter userRepositoryAdapter;

    @Operation(summary = "Authenticate a valid user")
    @GetMapping(path = "/auths",produces = "application/json")
    public ResponseEntity<Response<String>> getToken(@RequestHeader("user") String user, @RequestHeader("password") String password){

        var response = new Response<String>();

        var optionalUser = userRepositoryAdapter.findByUsernameAndPassword(user,password);

        if(optionalUser.isPresent()){
            //Lógica do Token
            response.setData("Token");
            return ResponseEntity.ok().body(response);
        }
        response.setData("Deu ruim");
        return ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Authenticate a valid user")
    @PostMapping(path = "/users",produces = "application/json")
    public ResponseEntity<Response<String>> saveUser(@RequestBody UserDto userDto){

        var response = new Response<String>();

        var optionalUser = userRepositoryAdapter.saveUser(userDto.getUsername(), userDto.getPassword());

        response.setData("deu ruim");

        return ResponseEntity.ok().body(response);
    }

}
