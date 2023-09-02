package com.example.api.dealership.adapter.dtos.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = ClientDtoUpdateRequest.ClientDtoUpdateRequestBuilder.class)
public final class ClientDtoUpdateRequest {

    @JsonProperty
    private final String postCode;

    @JsonProperty
    private final String streetNumber;

    private ClientDtoUpdateRequest(final String postCode, final String streetNumber) {
        this.postCode = postCode;
        this.streetNumber = streetNumber;
    }

    public static ClientDtoUpdateRequest createClientDtoUpdate(final String postCode, final String streetNumber){
        return new ClientDtoUpdateRequest(postCode,streetNumber);
    }
}
