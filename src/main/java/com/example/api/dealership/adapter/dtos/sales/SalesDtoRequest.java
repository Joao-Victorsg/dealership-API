package com.example.api.dealership.adapter.dtos.sales;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class SalesDtoRequest {

    @NotBlank
    private String cpf;

    @NotBlank
    private String vin;

}
