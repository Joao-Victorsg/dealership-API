package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClientDtoResponse {

    @NotBlank
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 11,max = 11)
    private String cpf;

    @NotNull
    private AddressDtoResponse address;

    @NotNull
    private LocalDateTime registrationDate;

}
