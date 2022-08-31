package com.example.api.dealership.core.dtos.client;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ClientDtoRequest {

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
    private String state;

    @NotBlank
    private String streetName;

    @NotBlank
    private String streetNumber;

}
