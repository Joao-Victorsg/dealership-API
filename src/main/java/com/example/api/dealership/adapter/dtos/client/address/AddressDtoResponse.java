package com.example.api.dealership.adapter.dtos.client.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressDtoResponse (
    @NotBlank @JsonProperty("cep")  String postCode,
    @JsonProperty("localidade") String city,
    @JsonProperty("uf") String stateAbbreviation,
    @JsonProperty("logradouro") String streetName,
    Boolean isAddressSearched
){

    @Builder
    public AddressDtoResponse(@NotBlank @JsonProperty("cep") String postCode,
                              @JsonProperty("localidade") String city,
                              @JsonProperty("uf") String stateAbbreviation,
                              @JsonProperty("logradouro") String streetName,
                              Boolean isAddressSearched) {
        this.postCode = postCode;
        this.city = city;
        this.stateAbbreviation = stateAbbreviation;
        this.streetName = streetName;
        this.isAddressSearched = verifyIfAddressWasSearched(isAddressSearched);
    }

    private boolean verifyIfAddressWasSearched(Boolean isAddressSearched){
        return ObjectUtils.anyNull(isAddressSearched);
    }
}
