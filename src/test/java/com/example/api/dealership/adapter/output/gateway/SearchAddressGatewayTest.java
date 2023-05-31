package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchAddressGatewayTest {

    @InjectMocks
    private SearchAddressGateway searchAddressGateway;

    @Mock
    private RestTemplate restTemplate;

    private final String URI_VIA_CEP = "https://viacep.com.br/ws/{cep}/json";

    @Test
    @DisplayName("Given a postcode, search the address")
    void givenPostCodeSearchTheAddress() {
        final var postCode = "111";
        final var params = new HashMap<String,String>();
        params.put("cep",postCode);
        final var address = new AddressDtoResponse();
        final var responseEntity = new ResponseEntity<>(address, HttpStatus.OK);

        when(restTemplate.getForEntity(URI_VIA_CEP, AddressDtoResponse.class, params))
                .thenReturn(responseEntity);

        final var resultAddress =searchAddressGateway.searchAddressByPostCode(postCode);

        verify(restTemplate,times(1)).getForEntity(URI_VIA_CEP, AddressDtoResponse.class,params);

        assertEquals(address,resultAddress);

    }
}