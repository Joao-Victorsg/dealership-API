package com.example.api.dealership.adapter.dtos.sales;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SalesDtoResponse {

    @NotBlank
    private UUID salesId;

    @NotNull
    private LocalDateTime salesRegistrationDate;

    @NotBlank
    private UUID carId;

    @NotBlank
    private String carModel;

    @NotBlank
    private String carModelYear;

    @NotBlank
    private String carMake;

    @NotBlank
    private String carColor;

    @NotBlank
    private String carVin;

    private BigDecimal carValue;

    @NotNull
    private LocalDateTime carRegistrationDate;

    @NotBlank
    private UUID clientId;

    @NotBlank
    private String clientName;

    @NotBlank
    @Size(min = 11,max = 11)
    private String clientCpf;

    @NotBlank
    private String clientCity;

    @NotBlank
    private String clientPostCode;

    @NotBlank
    private String clientState;

    @NotBlank
    private String clientStreetName;

    @NotBlank
    private String clientStreetNumber;

    @NotNull
    private LocalDateTime clientRegistrationDate;
}
