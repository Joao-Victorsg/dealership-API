package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class SearchAddressGateway {
    private final String URI_VIA_CEP;

    private final RestTemplate restTemplate;

    @Autowired
    public SearchAddressGateway(RestTemplate restTemplate, @Value("${via-cep.url}") String uriViaCep){
        this.URI_VIA_CEP = uriViaCep;
        this.restTemplate = restTemplate;
    }

    public AddressDtoResponse searchAddressByPostCode(String postCode){
        var params = new HashMap<String,String>();
        params.put("cep",postCode);
        return restTemplate.getForEntity(URI_VIA_CEP, AddressDtoResponse.class,params).getBody();
    }

}
