package com.example.api.dealership.adapter.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
