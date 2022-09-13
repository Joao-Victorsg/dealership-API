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
    private String carModel;

    @Column(nullable = false)
    private String carModelYear;

    @Column(nullable = false)
    private String carMake;

    @Column(nullable = false)
    private String carColor;

    @Column(nullable = false,unique = true)
    private String carVin;

    @Column(nullable = false)
    private Double carValue;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

}
