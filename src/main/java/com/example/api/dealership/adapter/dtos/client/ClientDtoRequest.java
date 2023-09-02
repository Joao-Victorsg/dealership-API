package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ClientDtoRequest.ClientDtoRequestBuilder.class)
public final class ClientDtoRequest {

    @NotBlank
    @JsonProperty
    private final String name;

    @NotBlank
    @JsonProperty
    private final String cpf;

    @NotNull
    @JsonProperty
    private final AddressDtoRequest address;

    private ClientDtoRequest(final String name, final String cpf, final @NotNull AddressDtoRequest address) {
        this.name = name;
        this.cpf = cpf;
        this.address = address;
    }

    public static ClientDtoRequest createClientDtoRequest(final String name, final String cpf,
                                                          final AddressDtoRequest addressDtoRequest){
        return new ClientDtoRequest(name,cpf,addressDtoRequest);
    }
}
