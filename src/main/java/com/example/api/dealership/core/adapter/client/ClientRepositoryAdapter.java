package com.example.api.dealership.core.adapter.client;

import com.example.api.dealership.core.domain.ClientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientRepositoryAdapter {

    Optional<ClientModel> findByCpf(String cpf);

    Page<ClientModel> getClients(Pageable pageable);

    ClientModel saveClient(ClientModel clientModel);

    void deleteClient(String cpf);
}
