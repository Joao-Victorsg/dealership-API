package com.example.api.dealership.core.dtos.car;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarDtoResponse {

    @NotBlank
    private UUID id;

    @NotBlank
    private String model;

    @NotBlank
    private String brand;

    @NotBlank
    private String color;

    @NotBlank
    private String vehicleIdentificationNumber;

    private BigDecimal value;

    @NotNull
    private LocalDateTime registrationDate;
}
