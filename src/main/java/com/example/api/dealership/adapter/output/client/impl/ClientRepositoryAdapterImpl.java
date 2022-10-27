package com.example.api.dealership.adapter.output.client.impl;

import com.example.api.dealership.adapter.output.client.ClientRepositoryAdapter;
import com.example.api.dealership.adapter.output.repository.ClientRepositoryPort;
import com.example.api.dealership.core.domain.ClientModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientRepositoryAdapterImpl implements ClientRepositoryAdapter {

    public final ClientRepositoryPort clientRepositoryPort;

    @Override
    public Optional<ClientModel> findByCpf(String cpf) {
        return clientRepositoryPort.findByCpf(cpf);
    }

    @Override
    public Page<ClientModel> getClients(Pageable pageable) {
        return clientRepositoryPort.findAll(pageable);
    }

    @Override
    @Transactional
    public ClientModel saveClient(ClientModel clientModel) {
        return clientRepositoryPort.save(clientModel);
    }

    @Override
    @Transactional
    public void deleteClient(String cpf) {
        clientRepositoryPort.deleteByCpf(cpf);
    }


}
