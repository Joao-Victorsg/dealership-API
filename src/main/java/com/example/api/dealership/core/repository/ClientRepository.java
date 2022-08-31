package com.example.api.dealership.core.repository;


import com.example.api.dealership.core.domain.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientModel, UUID> {

    Optional<ClientModel> findByCpf(String cpf);

    void deleteByCpf(String cpf);
}
