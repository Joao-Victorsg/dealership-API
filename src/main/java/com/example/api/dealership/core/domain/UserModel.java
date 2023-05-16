package com.example.api.dealership.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "TB_USER")
public class UserModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;

    @Column(nullable = false)
    @NotBlank
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }
}
