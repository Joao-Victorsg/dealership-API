package com.example.api.dealership.adapter.dtos.car;

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
    private String carModel;

    @NotBlank
    private String carModelYear;

    @NotBlank
    private String carMake;

    @NotBlank
    private String carColor;

    @NotBlank
    private String carVin;

    private BigDecimal carValue;

    @NotNull
    private LocalDateTime carRegistrationDate;
}
