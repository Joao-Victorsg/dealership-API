package com.example.api.dealership.adapter.dtos.client.address;

import lombok.Builder;

@Builder
public record AddressDtoResponse(
        String postCode,
        String city,
        String stateAbbreviation,
        String streetName,
        Boolean isAddressSearched
) {}
