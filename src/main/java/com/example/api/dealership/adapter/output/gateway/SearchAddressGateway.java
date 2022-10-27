package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SearchAddressGateway {

    private final String URI_VIA_CEP = "https://viacep.com.br/ws/{cep}/json";

    private final RestTemplate restTemplate;

    public AddressDtoResponse searchAddressByPostCode(String postCode){
        var params = new HashMap<String,String>();
        params.put("cep",postCode);
        return restTemplate.getForEntity(URI_VIA_CEP, AddressDtoResponse.class,params).getBody();
    }

}
