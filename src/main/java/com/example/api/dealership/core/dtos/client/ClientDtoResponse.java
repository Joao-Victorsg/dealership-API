package com.example.api.dealership.core.dtos.client;

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

    @NotBlank
    private String city;

    @NotBlank
    private String postCode;

    @NotBlank
    private String stateAbbreviation;

    @NotBlank
    private String streetName;

    @NotBlank
    private String streetNumber;

    @NotNull
    private LocalDateTime registrationDate;

}
