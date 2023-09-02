package com.example.api.dealership.adapter.dtos.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = CarDtoUpdateRequest.CarDtoUpdateRequestBuilder.class)
public class CarDtoUpdateRequest {

    @JsonProperty
    private final String color;

    @JsonProperty
    private final Double value;

    //TODO: Validate if the value is greater or equal zero.
    private CarDtoUpdateRequest(final String color, final Double value) {
        this.color = color;
        this.value = value;
    }
}