package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value= "via-cep-api", url="${via-cep.url}")
public interface SearchAddressGateway {
    @RequestMapping(method = RequestMethod.GET,value = "/{postCode}/json", produces = "application/json")
    AddressDtoResponse searchAddressByPostCode(@PathVariable("postCode") String postCode);
}
