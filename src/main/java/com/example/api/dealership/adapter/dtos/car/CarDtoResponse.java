package com.example.api.dealership.adapter.dtos.car;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@JsonDeserialize(builder = CarDtoResponse.CarDtoResponseBuilder.class)
public record CarDtoResponse(
        @NotBlank UUID id,
        @NotBlank String model,
        @NotBlank String modelYear,
        @NotBlank String manufacturer,
        @NotBlank String color,
        @NotBlank String vin,
        BigDecimal value,
        @NotNull  LocalDateTime registrationDate
) {}
