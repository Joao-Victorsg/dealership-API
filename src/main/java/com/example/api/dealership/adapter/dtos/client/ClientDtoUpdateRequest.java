package com.example.api.dealership.adapter.dtos.client;

import lombok.Getter;

@Getter
public final class ClientDtoUpdateRequest {

    private final String postCode;

    private final String streetNumber;

    private ClientDtoUpdateRequest(final String postCode, final String streetNumber) {
        this.postCode = postCode;
        this.streetNumber = streetNumber;
    }

    public static ClientDtoUpdateRequest createClientDtoUpdate(final String postCode, final String streetNumber){
        return new ClientDtoUpdateRequest(postCode,streetNumber);
    }
}
