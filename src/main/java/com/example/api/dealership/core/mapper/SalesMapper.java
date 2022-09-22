package com.example.api.dealership.core.mapper;

import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.domain.SalesModel;
import com.example.api.dealership.core.dtos.client.ClientDtoResponse;
import com.example.api.dealership.core.dtos.sales.SalesDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface SalesMapper {

    default SalesModel toSalesModel(CarModel car, ClientModel client){
        var sales = new SalesModel();
        sales.setCar(car);
        sales.setClient(client);
        sales.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return sales;
    }

    @Mapping(source = "client.id",target = "clientId")
    @Mapping(source = "client.name",target = "clientName")
    @Mapping(source = "client.cpf",target = "clientCpf")
    @Mapping(source = "client.address.city",target = "clientCity")
    @Mapping(source = "client.address.postCode",target = "clientPostCode")
    @Mapping(source = "client.address.stateAbbreviation",target = "clientState")
    @Mapping(source = "client.address.streetName",target = "clientStreetName")
    @Mapping(source = "client.address.streetNumber",target = "clientStreetNumber")
    @Mapping(source = "client.registrationDate",target = "clientRegistrationDate")
    @Mapping(source = "car",target = ".")
    @Mapping(source = "car.id",target = "carId")
    @Mapping(source = "id",target = "salesId")
    @Mapping(source = "registrationDate",target = "salesRegistrationDate")
    SalesDtoResponse toSalesDtoResponse(SalesModel sales);

}
