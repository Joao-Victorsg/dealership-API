package com.example.api.dealership.adapter.output.gateway;

import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchAddressGatewayTest {

    @Mock
    private SearchAddressGateway addressGateway;

    @Test
    @DisplayName("Given a postcode, search the address")
    void givenPostCodeSearchTheAddress() {
        final var postCode = "111";
        final var address = new AddressDtoResponse();

        when(addressGateway.searchAddressByPostCode(postCode))
                .thenReturn(address);

        final var resultAddress = addressGateway.searchAddressByPostCode(postCode);

        verify(addressGateway,times(1)).searchAddressByPostCode(postCode);

        assertEquals(address,resultAddress);
    }
}