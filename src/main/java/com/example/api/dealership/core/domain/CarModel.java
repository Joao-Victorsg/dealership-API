package com.example.api.dealership.core.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private String brand;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false,unique = true)
    private String vehicleIdentificationNumber;

    @Column(nullable = false)
    private BigDecimal carValue;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

}
