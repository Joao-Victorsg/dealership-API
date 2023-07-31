package com.example.api.dealership.adapter.dtos.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
