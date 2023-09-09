package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
@JsonDeserialize(builder = ClientDtoRequest.ClientDtoRequestBuilder.class)
public record ClientDtoRequest (
    @NotBlank @JsonProperty String name,
    @NotBlank @JsonProperty String cpf,
    @NotNull @JsonProperty AddressDtoRequest address
){}
