package com.example.api.dealership.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@Table(name="TB_ADDRESS")
public class AddressModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;

    @NotBlank
    private String city;

    @NotBlank
    private String postCode;

    @NotBlank
    private String state;

    @NotBlank
    private String streetName;

    @NotBlank
    private String streetNumber;
}
