package com.example.api.dealership.adapter.dtos.sales;

import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SalesDtoResponse {

    @NotBlank
    private UUID id;

    @NotNull
    private LocalDateTime registrationDate;

    @NotNull
    private CarDtoResponse car;

    @NotNull
    private ClientDtoResponse client;
}
