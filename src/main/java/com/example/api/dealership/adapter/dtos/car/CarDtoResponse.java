package com.example.api.dealership.adapter.dtos.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CarDtoResponse {

    @NotBlank
    private final UUID id;

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

    private final BigDecimal value;

    @NotNull
    private final LocalDateTime registrationDate;

    private CarDtoResponse(final UUID id, final String model, final String modelYear, final String manufacturer,
                           final String color, final String vin, final BigDecimal value,
                           @NotNull final LocalDateTime registrationDate) {
        this.id = id;
        this.model = model;
        this.modelYear = modelYear;
        this.manufacturer = manufacturer;
        this.color = color;
        this.vin = vin;
        this.value = value;
        this.registrationDate = registrationDate;
    }

    public CarDtoResponse carDtoResponse(final UUID id, final String model, final String modelYear,
                                         final String manufacturer, final String color, final String vin,
                                         final BigDecimal value,
                                         @NotNull final LocalDateTime registrationDate){
        return new CarDtoResponse(id, model, modelYear, manufacturer, color, vin, value, registrationDate);
    }
}
