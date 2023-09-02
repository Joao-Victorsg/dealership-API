package com.example.api.dealership.adapter.dtos.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@JsonDeserialize(builder = CarDtoRequest.CarDtoRequestBuilder.class)
public class CarDtoRequest {

    @NotBlank
    @JsonProperty
    private final String model;

    @NotBlank
    @JsonProperty
    private final String modelYear;

    @NotBlank
    @JsonProperty
    private final String manufacturer;

    @NotBlank
    @JsonProperty
    private final String color;

    @NotBlank
    @JsonProperty
    private final String vin;

    @JsonProperty
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
