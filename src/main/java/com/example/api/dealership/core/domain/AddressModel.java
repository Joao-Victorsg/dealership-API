package com.example.api.dealership.core.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name="TB_ADDRESS")
public class AddressModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;


    private String city;

    @NotBlank
    private String postCode;

    @NotBlank
    private String stateAbbreviation;

    @NotBlank
    private String streetName;

    @NotBlank
    private String streetNumber;
}
