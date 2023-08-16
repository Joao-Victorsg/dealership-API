package com.example.api.dealership.adapter.dtos.client;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public final class ClientDtoResponse {

    @NotBlank
    private final UUID id;

    @NotBlank
    private final String name;

    @NotBlank
    @Size(min = 11,max = 11)
    private final String cpf;

    @NotNull
    private final AddressDtoResponse address;

    @NotNull
    private final LocalDateTime registrationDate;

    private ClientDtoResponse(final UUID id, final String name, final String cpf,
                             final @NotNull AddressDtoResponse address, final @NotNull LocalDateTime registrationDate) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.address = address;
        this.registrationDate = registrationDate;
    }

    public static ClientDtoResponse createClientDtoResponse(final UUID id, final String name,
                                                            final String cpf, final @NotNull AddressDtoResponse address,
                                                            final @NotNull LocalDateTime registrationDate){
        return new ClientDtoResponse(id,name,cpf,address,registrationDate);
    }
}
