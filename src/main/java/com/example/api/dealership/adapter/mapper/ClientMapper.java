package com.example.api.dealership.adapter.mapper;


import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.ClientModel;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class ClientMapper {

    private ClientMapper(){

    }

    public static ClientModel toClientModel(ClientDtoRequest request){
        return ClientModel.builder()
                .cpf(request.cpf())
                .name(request.name())
                .address(toAddressModel(request.address()))
                .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
    }
    public static ClientDtoResponse toClientDtoResponse(ClientModel clientModel){
        return ClientDtoResponse.builder()
                .name(clientModel.getName())
                .id(clientModel.getId())
                .cpf(clientModel.getCpf())
                .registrationDate(clientModel.getRegistrationDate())
                .address(toAddressDtoResponse(clientModel.getAddress()))
                .build();
    }

    public static AddressModel toAddressModel(AddressDtoRequest address){
        return AddressModel.builder()
                .postCode(address.postCode())
                .streetNumber(address.streetNumber())
                .build();
    }

    private static AddressDtoResponse toAddressDtoResponse(AddressModel addressModel){

        return AddressDtoResponse.builder()
                .postCode(addressModel.getPostCode())
                .city(addressModel.getCity())
                .stateAbbreviation(addressModel.getStateAbbreviation())
                .streetName(addressModel.getStreetName())
                .isAddressSearched(addressModel.getIsAddressSearched())
                .build();
    }
}
