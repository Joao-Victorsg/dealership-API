package com.example.api.dealership.adapter.service.client;

import com.example.api.dealership.core.domain.ClientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientService {

    Optional<ClientModel> findByCpf(String cpf);

    Page<ClientModel> getClients(String city,String state, Pageable pageable);

    ClientModel saveClient(ClientModel clientModel);

    void deleteClient(String cpf);
}
