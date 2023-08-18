package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.domain.SalesModel;
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

    @Mapping(source = "car", target = "car.")
    @Mapping(source = "client", target = "client.")
    @Mapping(source = "client.address",target = "client.address.")
    @Mapping(source = "client.address.addressSearched", target = "client.address.isAddressSearched")
    SalesDtoResponse toSalesDtoResponse(SalesModel sales);

}
