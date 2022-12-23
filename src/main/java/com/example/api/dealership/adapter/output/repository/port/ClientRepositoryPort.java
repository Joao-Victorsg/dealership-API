package com.example.api.dealership.adapter.output.repository.port;


import com.example.api.dealership.core.domain.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepositoryPort extends JpaRepository<ClientModel, UUID> {

    Optional<ClientModel> findByCpf(String cpf);

    void deleteByCpf(String cpf);
}
