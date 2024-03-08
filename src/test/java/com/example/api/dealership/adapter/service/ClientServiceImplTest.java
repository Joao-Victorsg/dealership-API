package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.dtos.client.ClientDtoUpdateRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import com.example.api.dealership.adapter.output.gateway.SearchAddressGateway;
import com.example.api.dealership.adapter.output.repository.port.ClientRepositoryPort;
import com.example.api.dealership.adapter.service.client.impl.ClientServiceImpl;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Mock
    private SearchAddressGateway searchAddressGateway;

    @Test
    @DisplayName("Given a valid CPF return a client")
    void givenValidCPFReturnAClient() {
        final var expectedClient = ClientModel.builder().build();

        when(clientRepositoryPort.findByCpf("123")).thenReturn(Optional.of(expectedClient));

        final var client = clientService.findByCpf("123");

        assertEquals(client.get(),expectedClient);
    }

    @Test
    @DisplayName("Given a request without filters return all the clients")
    void givenARequestWithoutFiltersReturnAllTheClients() {
        final var clientsList = getClientsList();
        final var expectedClientPage = new PageImpl<>(clientsList);
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients(null,null,pageable);

        assertEquals(clients,expectedClientPage);
    }

    @Test
    @DisplayName("Given a city return the clients that are from this city")
    void givenACityReturnTheClientsThatAreFromThisCity() {
        final var clientsList = getClientsList();
        final var expectedClientPage = new PageImpl<>(List.of(clientsList.get(0)));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients("Muriae",null,pageable);

        assertEquals(clients,expectedClientPage);
        assertEquals(1L,clients.getSize());
        assertEquals("Muriae",clients.getContent().get(0).getAddress().getCity());
    }

    @Test
    @DisplayName("Given a state abbreviation return the clients that are from this state")
    void givenAStateAbbreviationReturnTheClientsThatAreFromThisState() {
        final var clientsList = getClientsList();
        final var expectedClientPage = new PageImpl<>(clientsList.subList(0,2));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients(null,"MG",pageable);

        assertEquals(clients,expectedClientPage);
    }

    @Test
    @DisplayName("Given a state abbreviation and a city return the clients that are from this state and city")
    void givenStateAbbreviationAndCityReturnClientsThatAreFromThisStateAndThisCity() {
        final var expectedClientPage = new PageImpl<>(List.of());
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients("Muriae","MG",pageable);

        assertEquals(0,clients.getSize());
    }

    @Test
    @DisplayName("Given a client that doesn't exists save it in the database")
    void givenClientThatDoesNotExistsSaveItInTheDatabase() {
        final var expectedClient = ClientModel.builder()
                .cpf("123")
                .address(AddressModel.builder().postCode("123").build())
                .build();

        when(clientRepositoryPort.findByCpf("123")).thenReturn(Optional.empty());
        when(searchAddressGateway.byPostCode("123")).thenReturn(AddressDtoResponse.builder().isAddressSearched(true).build());
        when(clientRepositoryPort.save(expectedClient)).thenReturn(expectedClient);

        assertDoesNotThrow(() -> {
            final var responseClient = clientService.saveClient(expectedClient);
            assertEquals(expectedClient,responseClient);
        });

        verify(clientRepositoryPort).save(expectedClient);
    }

    @Test
    @DisplayName("Given a client that already exists when trying to save, throw DuplicatedInfoException ")
    void givenClientThatAlreadyExistsWhenTryingToSaveThenThrowDuplicatedInfoException() {
        final var expectedClient = ClientModel.builder().cpf("123").build();

        when(clientRepositoryPort.findByCpf("123")).thenReturn(Optional.of(expectedClient));

        assertThrows(DuplicatedInfoException.class,() -> clientService.saveClient(expectedClient));
    }

    @Test
    @DisplayName("Given a existent client CPF delete it from the database")
    void givenExistentClientCpfDeleteItFromTheDatabase() {
        when(clientRepositoryPort.findByCpf("123456789")).thenReturn(Optional.of(ClientModel.builder().build()));

        assertDoesNotThrow(() ->clientService.deleteClient("123456789"));

        verify(clientRepositoryPort).deleteByCpf("123456789");
    }

    @Test
    @DisplayName("Given a nonexistent client CPF when trying to delete throw ClientNotFoundException")
    void givenNonExistentClientCpfWhenTryingToDeleteThrowClientNotFoundException() {
        when(clientRepositoryPort.findByCpf("123456789")).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,() ->clientService.deleteClient("123456789"));
    }
    @Test
    @DisplayName("Given an existent CPF, update the client information")
    void givenExistentCPFUpdateTheClientInformation(){
        final var clientUpdateDtoRequest = ClientDtoUpdateRequest.builder()
                .streetNumber("123")
                .postCode("36666-666")
                .build();
        final var oldClient = ClientModel.builder().cpf("123456789")
                .address(AddressModel.builder()
                        .streetNumber("123")
                        .postCode("37777-777")
                        .build())
                .build();
        final var updatedCliente = ClientModel.builder().cpf("123456789")
                .address(AddressModel.builder()
                        .streetNumber("123")
                        .postCode("36666-666")
                        .build())
                .build();

        when(clientRepositoryPort.findByCpf("123456789")).thenReturn(Optional.of(oldClient));
        when(searchAddressGateway.byPostCode(clientUpdateDtoRequest.postCode()))
                .thenReturn(AddressDtoResponse.builder().postCode("36666-666").build());
        when(clientRepositoryPort.save(any(ClientModel.class))).thenReturn(updatedCliente);

        assertDoesNotThrow(() -> {
            final var response = clientService.updateClient(oldClient.getCpf(),clientUpdateDtoRequest);
            assertEquals(updatedCliente,response);
        });
    }

    @Test
    @DisplayName("Given an nonexistente CPF, when trying to update the client information throw ClientNotFoundException")
    void givenNonExistentCPFWhenTryingToUpdateClientInformationThrowClientNotFoundException(){
        final var clientUpdateDtoRequest = ClientDtoUpdateRequest.builder()
                .streetNumber("123")
                .postCode("36666-666")
                .build();

        when(clientRepositoryPort.findByCpf("123456789")).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () ->
                clientService.updateClient("123456789",clientUpdateDtoRequest));
    }

    private List<ClientModel> getClientsList(){
        return List.of(
                getClient("Muriae","MG"),
                getClient("Leopoldina","MG"),
                getClient("Sao Paulo","SP")
        );
    }

    private ClientModel getClient(String city, String stateAbbreviation){
        return ClientModel.builder()
                .address(AddressModel.builder()
                        .city(city)
                        .stateAbbreviation(stateAbbreviation)
                        .build())
                .build();
    }

}
