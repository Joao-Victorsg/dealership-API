package com.example.api.dealership.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
