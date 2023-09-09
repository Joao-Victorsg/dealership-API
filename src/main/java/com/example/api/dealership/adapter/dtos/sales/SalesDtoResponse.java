package com.example.api.dealership.adapter.dtos.sales;

import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SalesDtoResponse(@NotBlank UUID id,
                               @NotNull LocalDateTime registrationDate,
                               @NotNull CarDtoResponse car,
                               @NotNull ClientDtoResponse client) {

}
