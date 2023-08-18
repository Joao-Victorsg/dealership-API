package com.example.api.dealership.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CARMODEL")
public class CarModel{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String modelYear;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false,unique = true)
    private String vin;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

}
