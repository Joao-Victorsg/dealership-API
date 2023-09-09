package com.example.api.dealership.adapter.dtos.sales;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
@Builder
public record SalesDtoRequest(@NotBlank  String cpf, @NotBlank String vin) {

}
