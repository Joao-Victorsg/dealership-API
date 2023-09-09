package com.example.api.dealership.adapter.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = UserDtoRequest.UserDtoRequestBuilder.class)
public record UserDtoRequest (
    @NotBlank @JsonProperty String username,
    @NotBlank @JsonProperty String password
){}