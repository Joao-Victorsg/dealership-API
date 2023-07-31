package com.example.api.dealership.adapter.dtos.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDtoRequest {

    @NotBlank
    private String cpf;

    @NotBlank
    private String vin;

}
