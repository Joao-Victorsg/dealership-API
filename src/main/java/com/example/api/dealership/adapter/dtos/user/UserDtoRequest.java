package com.example.api.dealership.adapter.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}