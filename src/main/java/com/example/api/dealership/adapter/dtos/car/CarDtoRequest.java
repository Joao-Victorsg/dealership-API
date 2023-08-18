package com.example.api.dealership.adapter.dtos.car;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class CarDtoRequest {

    @NotBlank
    private final String model;

    @NotBlank
    private final String modelYear;

    @NotBlank
    private final String manufacturer;

    @NotBlank
    private final String color;

    @NotBlank
    private final String vin;

    private final Double value;

    private CarDtoRequest(final String model, final String modelYear, final String manufacturer,
                          final String color, final String vin, final Double value) {
        this.model = model;
        this.modelYear = modelYear;
        this.manufacturer = manufacturer;
        this.color = color;
        this.vin = vin;
        this.value = value;
    }
}
