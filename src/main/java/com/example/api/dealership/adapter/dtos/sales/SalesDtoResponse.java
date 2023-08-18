package com.example.api.dealership.adapter.dtos.sales;

import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SalesDtoResponse {

    @NotBlank
    private final UUID id;

    @NotNull
    private final LocalDateTime registrationDate;

    @NotNull
    private final CarDtoResponse car;

    @NotNull
    private final ClientDtoResponse client;

    private SalesDtoResponse(final UUID id, @NotNull final LocalDateTime registrationDate,
                             @NotNull final CarDtoResponse car, @NotNull final ClientDtoResponse client) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.car = car;
        this.client = client;
    }
}
