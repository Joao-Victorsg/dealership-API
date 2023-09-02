package com.example.api.dealership.adapter.dtos.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = SalesDtoRequest.SalesDtoRequestBuilder.class)
public class SalesDtoRequest {

    @NotBlank
    @JsonProperty
    private final String cpf;

    @NotBlank
    @JsonProperty
    private final String vin;

    private SalesDtoRequest(final String cpf, final String vin) {
        this.cpf = cpf;
        this.vin = vin;
    }
}
