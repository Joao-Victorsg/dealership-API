package com.example.api.dealership.adapter.dtos.car;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CarDtoUpdateRequest {

    private final String color;

    private final Double value;

    //TODO: Validate if the value is greater or equal zero.
    private CarDtoUpdateRequest(final String color, final Double value) {
        this.color = color;
        this.value = value;
    }
}