package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientDtoRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String cpf;

    @NotNull
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
