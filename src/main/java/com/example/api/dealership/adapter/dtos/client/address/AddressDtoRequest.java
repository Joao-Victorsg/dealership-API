package com.example.api.dealership.adapter.dtos.client.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public final class AddressDtoRequest {

    @NotBlank
    private final String streetNumber;

    @NotBlank
    private final String postCode;

    private AddressDtoRequest(final String streetNumber, final String postCode) {
        this.streetNumber = streetNumber;
        this.postCode = postCode;
    }

    public static AddressDtoRequest createAddress(final String streetNumber, final String postCode){
        return new AddressDtoRequest(streetNumber,postCode);
    }
}
