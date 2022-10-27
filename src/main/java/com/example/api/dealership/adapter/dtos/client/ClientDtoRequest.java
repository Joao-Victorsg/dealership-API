package com.example.api.dealership.adapter.dtos.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDtoRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    private String city;

    private String stateAbbreviation;

    private String streetName;

    @NotBlank
    private String streetNumber;

    @NotBlank
    private String postCode;


}
