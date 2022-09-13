package com.example.api.dealership.core.mapper;

import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.dtos.client.ClientDtoRequest;
import com.example.api.dealership.core.dtos.client.ClientDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface ClientMapper {


    @Mapping(source = "streetNumber",target = "address.streetNumber")
    @Mapping(source = "streetName",target = "address.streetName")
    @Mapping(source = "state",target = "address.state")
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
