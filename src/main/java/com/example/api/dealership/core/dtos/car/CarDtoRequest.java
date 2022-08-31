package com.example.api.dealership.core.dtos.car;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CarDtoRequest {

    @NotBlank
    private String model;

    @NotBlank
    private String brand;

    @NotBlank
    private String color;

    @NotBlank
    private String vehicleIdentificationNumber;

    private BigDecimal value;

}
