package com.example.api.dealership.adapter.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDtoRequest {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    private UserDtoRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public static UserDtoRequest createUserDtoRequest(final String username, final String password){
        return new UserDtoRequest(username,password);
    }

}