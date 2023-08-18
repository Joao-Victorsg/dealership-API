package com.example.api.dealership.adapter.dtos.sales;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesDtoRequest {

    @NotBlank
    private final String cpf;

    @NotBlank
    private final String vin;

    private SalesDtoRequest(final String cpf, final String vin) {
        this.cpf = cpf;
        this.vin = vin;
    }
}
