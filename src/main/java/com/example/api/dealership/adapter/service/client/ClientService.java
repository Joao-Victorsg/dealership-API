package com.example.api.dealership.adapter.service.client;

import com.example.api.dealership.adapter.dtos.client.ClientDtoUpdateRequest;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientService {

    Optional<ClientModel> findByCpf(final String cpf);

    Page<ClientModel> getClients(final String city,final String state, final Pageable pageable);

    ClientModel saveClient(final ClientModel clientModel) throws DuplicatedInfoException;

    void deleteClient(final String cpf) throws ClientNotFoundException;

    ClientModel updateClient(final String cpf, final ClientDtoUpdateRequest clientDtoUpdateRequest) throws ClientNotFoundException;
}
