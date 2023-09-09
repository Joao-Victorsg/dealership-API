package com.example.api.dealership.adapter.dtos.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder(toBuilder = true)
@JsonDeserialize(builder = CarDtoRequest.CarDtoRequestBuilder.class)
public record CarDtoRequest (
        @JsonProperty @NotBlank String model,
        @JsonProperty @NotBlank String modelYear,
        @JsonProperty @NotBlank String manufacturer,
        @JsonProperty @NotBlank String color,
        @JsonProperty @NotBlank String vin,
        @JsonProperty Double value){
}
