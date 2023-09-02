package com.example.api.dealership.adapter.dtos.client.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = AddressDtoRequest.AddressDtoRequestBuilder.class)
public final class AddressDtoRequest {

    @NotBlank
    @JsonProperty
    private final String streetNumber;

    @NotBlank
    @JsonProperty
    private final String postCode;

    private AddressDtoRequest(final String streetNumber, final String postCode) {
        this.streetNumber = streetNumber;
        this.postCode = postCode;
    }

    public static AddressDtoRequest createAddress(final String streetNumber, final String postCode){
        return new AddressDtoRequest(streetNumber,postCode);
    }
}
