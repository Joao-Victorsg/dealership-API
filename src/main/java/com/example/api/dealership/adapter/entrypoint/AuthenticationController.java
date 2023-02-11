package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.config.rest.token.validator.Token;
import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.output.repository.adapter.user.UserRepositoryAdapter;
import de.mkammerer.argon2.Argon2Factory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RestController
@RequestMapping(path = "/v1/dealership")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepositoryAdapter userRepositoryAdapter;

    private final UserMapper userMapper;

    @Operation(summary = "Authenticate a valid user")
    @GetMapping(path = "/auths",produces = "application/json")
    public ResponseEntity<Response<String>> getToken(@RequestHeader("user") String user, @RequestHeader("password") String password){

        var response = new Response<String>();

        var hasher = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32,64);

        var optionalUser = userRepositoryAdapter.findByUsername(user);

        if(optionalUser.isPresent()){
            if(hasher.verify(optionalUser.get().getPassword(),password.toCharArray())) {
                response.setData(generateJWToken(user));
                return ResponseEntity.ok().body(response);
            }
        }
        response.setData("Deu ruim");
        return ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Create a user")
    @PostMapping(path = "/users",produces = "application/json")
    public ResponseEntity<Response<String>> saveUser(@RequestBody UserDtoRequest userDtoRequest){

        var response = new Response<String>();

        var optinalUser = userRepositoryAdapter.findByUsername(userDtoRequest.getUsername());

        if (optinalUser.isEmpty()) {
            var userModel = userMapper.toUserModel(userDtoRequest);

            var user = userRepositoryAdapter.saveUser(userModel);

            response.setData("User: " + user.getUsername() + " created with success");

            return ResponseEntity.ok().body(response);
        }
        response.setData("This username already exists.");

        return ResponseEntity.ok().body(response);
    }

    private String generateJWToken(String username){

        final var payload = new JSONObject();
        payload.put("sub",username);

        final var tokenExpirationDate = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC);
        payload.put("exp",tokenExpirationDate);

        return new Token(payload).toString();
    }

}
