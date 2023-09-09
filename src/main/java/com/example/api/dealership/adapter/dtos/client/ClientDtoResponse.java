package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ClientDtoResponse (
    @NotBlank UUID id,
    @NotBlank String name,
    @NotBlank @Size(min = 11,max = 11) String cpf,
    @NotNull AddressDtoResponse address,
    @NotNull LocalDateTime registrationDate
){}
