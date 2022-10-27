package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.core.domain.ClientModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface ClientMapper {


    @Mapping(source = "streetNumber",target = "address.streetNumber")
    @Mapping(source = "streetName",target = "address.streetName")
    @Mapping(source = "stateAbbreviation",target = "address.stateAbbreviation")
    @Mapping(source = "postCode", target = "address.postCode")
    @Mapping(source = "city", target = "address.city")
    @Mapping(expression = "java(getRegistrationDate())",target = "registrationDate")
    ClientModel toClientModel(ClientDtoRequest request);

    default LocalDateTime getRegistrationDate(){
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    @Mapping(source = "address",target = ".")
    ClientDtoResponse toClientDtoResponse(ClientModel clientModel);

}
