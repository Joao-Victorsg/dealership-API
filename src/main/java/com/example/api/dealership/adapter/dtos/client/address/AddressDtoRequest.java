package com.example.api.dealership.adapter.dtos.client.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = AddressDtoRequest.AddressDtoRequestBuilder.class)
public record AddressDtoRequest (
    @NotBlank @JsonProperty String streetNumber,
    @NotBlank @JsonProperty String postCode
){}
