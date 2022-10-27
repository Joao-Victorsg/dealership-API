package com.example.api.dealership.adapter.dtos.car;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CarDtoRequest {

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

    private Double carValue;

}
