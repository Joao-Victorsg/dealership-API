package com.example.api.dealership.adapter.dtos.car;

import lombok.Builder;

@Builder
public record CarDtoUpdateRequest(String color, Double value) {}