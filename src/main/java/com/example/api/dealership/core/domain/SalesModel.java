package com.example.api.dealership.core.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="TB_SALESMODEL")
public class SalesModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientModel client;

    @OneToOne
    @JoinColumn(name = "car_id", unique = true, nullable = false)
    private CarModel car;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

}
