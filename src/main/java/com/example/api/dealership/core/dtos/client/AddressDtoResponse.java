package com.example.api.dealership.core.dtos.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDtoResponse {

    @NotBlank
    @JsonProperty("cep")
    private String postCode;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String stateAbbreviation;

    @JsonProperty("logradouro")
    private String streetName;

}
