package com.example.api.dealership.adapter.dtos.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = ClientDtoUpdateRequest.ClientDtoUpdateRequestBuilder.class)
public record ClientDtoUpdateRequest (
    @JsonProperty String postCode,
    @JsonProperty String streetNumber
){}
