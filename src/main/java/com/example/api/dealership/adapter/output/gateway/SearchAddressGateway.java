package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "via-cep-api", url = "${via-cep.url}")
public interface SearchAddressGateway {

    @CircuitBreaker(name="SearchAddressGatewaybyPostCode", fallbackMethod = "byPostCodeFallback")
    @GetMapping(value = "{postCode}/json", produces = "application/json")
    AddressDtoResponse byPostCode(@PathVariable("postCode") String postCode);

    default AddressDtoResponse byPostCodeFallback(String postCode, Exception ex){
        return AddressDtoResponse.builder()
                .postCode(postCode)
                .isAddressSearched(false)
                .build();
    }
}