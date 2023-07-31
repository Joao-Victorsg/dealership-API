package com.example.api.dealership.adapter.dtos.car;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
