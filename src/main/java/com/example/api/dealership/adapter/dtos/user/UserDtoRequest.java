package com.example.api.dealership.adapter.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UserDtoRequest.UserDtoRequestBuilder.class)
public class UserDtoRequest {

    @NotBlank
    @JsonProperty
    private final String username;

    @NotBlank
    @JsonProperty
    private final String password;
}