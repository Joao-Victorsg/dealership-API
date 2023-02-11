package com.example.api.dealership.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import javax.persistence.Entity;
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
    private LocalDateTime carRegistrationDate;

}
