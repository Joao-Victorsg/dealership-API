package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.ClientRepositoryPort;
import com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory;
import com.example.api.dealership.adapter.service.client.impl.ClientServiceImpl;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.ClientModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory.equalCity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Captor
    private ArgumentCaptor<Specification<ClientModel>> specificationArgumentCaptor;

    @Test
    @DisplayName("Given a valid CPF return a client")
    void givenValidCPFReturnAClient() {
        final var expectedClient = ClientModel.builder().build();

        when(clientRepositoryPort.findByCpf("123")).thenReturn(Optional.of(expectedClient));

        final var client = clientService.findByCpf("123");

        verify(clientRepositoryPort).findByCpf("123");
        verifyNoMoreInteractions(clientRepositoryPort);

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

        verify(clientRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(clientRepositoryPort);

        assertEquals(clients,expectedClientPage);
    }

    @Test
    @DisplayName("Given a city return the clients that are from this city")
    void givenACityReturnTheClientsThatAreFromThisCity() {
        final var clientsList = getClientsList();
        final var expectedClientPage = new PageImpl<>(List.of(clientsList.get(0)));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(eq(equalCity("Muriae")),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients("Muriae",null,pageable);

        verify(clientRepositoryPort).findAll(specificationArgumentCaptor.capture(),eq(pageable));
        verifyNoMoreInteractions(clientRepositoryPort);

        final var spec = specificationArgumentCaptor.getValue();

        assertEquals(spec,equalCity("Muriae"));
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

        verify(clientRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(clientRepositoryPort);

        assertEquals(clients,expectedClientPage);
    }

    @Test
    @DisplayName("Given a state abbreviation and a city return the clients that are from this state and city")
    void givenStateAbbreviationAndCityReturnClientsThatAreFromThisStateAndThisCity() {
        final var clientsList = getClientsList();
        final var expectedClientPage = new PageImpl<>(List.of());
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(clientRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedClientPage);

        final var clients = clientService.getClients("Muriae","MG",pageable);

        verify(clientRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(clientRepositoryPort);

        assertEquals(clients.getSize(),0);
    }

    @Test
    @DisplayName("Given a client save it in the database")
    void givenAClientSaveItInTheDatabase() {
        final var expectedClient = ClientModel.builder().build();

        when(clientRepositoryPort.save(expectedClient)).thenReturn(expectedClient);

        final var client = clientService.saveClient(expectedClient);

        verify(clientRepositoryPort).save(expectedClient);
        verifyNoMoreInteractions(clientRepositoryPort);

        assertEquals(client,expectedClient);
    }

    @Test
    @DisplayName("Given a client CPF delete it from the database")
    void givenAClientCpfDeleteItFromTheDatabase() {
        doNothing().when(clientRepositoryPort).deleteByCpf("123");

        clientService.deleteClient("123");

        verify(clientRepositoryPort).deleteByCpf("123");
        verifyNoMoreInteractions(clientRepositoryPort);
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