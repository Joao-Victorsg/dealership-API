package com.example.api.dealership.core.adapter.client.impl;

import com.example.api.dealership.core.adapter.client.ClientRepositoryAdapter;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientRepositoryAdapterImpl implements ClientRepositoryAdapter {

    public final ClientRepository clientRepository;

    @Override
    public Optional<ClientModel> findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf);
    }

    @Override
    public Page<ClientModel> getClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public ClientModel saveClient(ClientModel clientModel) {
        return clientRepository.save(clientModel);
    }

    @Override
    @Transactional
    public void deleteClient(String cpf) {
        clientRepository.deleteByCpf(cpf);
    }


}
