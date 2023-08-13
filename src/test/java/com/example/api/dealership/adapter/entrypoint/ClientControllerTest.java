package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import com.example.api.dealership.adapter.mapper.ClientMapper;
import com.example.api.dealership.adapter.output.gateway.SearchAddressGateway;
import com.example.api.dealership.adapter.service.client.ClientService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {


    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private SearchAddressGateway addressGateway;

    @Test
    @DisplayName("Given a valid client request, save it in the database")
    void givenValidClientRequestSaveItInTheDatabase() throws DuplicatedInfoException {
        final var clientDto = new ClientDtoRequest();
        final var clientAddress = new AddressDtoResponse();
        final var clientModel = new ClientModel();
        final var clientDtoResponse = new ClientDtoResponse();
        final var response = new Response<>();
        response.setData(clientDtoResponse);
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/clients/" + clientModel.getCpf()))
                .body(response);

        when(clientService.findByCpf(clientDto.getCpf())).thenReturn(Optional.empty());
        when(addressGateway.byPostCode(clientDto.getPostCode())).thenReturn(clientAddress);
        when(clientMapper.toClientModel(clientDto)).thenReturn(clientModel);
        when(clientService.saveClient(clientModel)).thenReturn(clientModel);
        when(clientMapper.toClientDtoResponse(clientModel)).thenReturn(clientDtoResponse);

        final var savedClient = clientController.saveClient(clientDto);

        verify(clientService).findByCpf(clientDto.getCpf());
        verify(clientMapper).toClientModel(clientDto);
        verify(clientService).saveClient(clientModel);
        verify(clientMapper).toClientDtoResponse(clientModel);

        assertEquals(HttpStatus.CREATED,savedClient.getStatusCode());
        assertEquals(expectedResponse.getBody().getData(),savedClient.getBody().getData());
    }

    @Test
    @DisplayName("Given a client request with a CPF that already exists, throw DuplicatedInfoException ")
    void givenClientRequestWithVinThatAlreadyExistsThrowDuplicatedInfoException(){
        final var clientDto = new ClientDtoRequest();
        final var clientModel = new ClientModel();

        when(clientService.findByCpf(clientDto.getCpf())).thenReturn(Optional.of(clientModel));

        assertThrows(DuplicatedInfoException.class, () -> clientController.saveClient(clientDto));
    }

    @Test
    @DisplayName("Get a page of clients from the database")
    void getAPageOfClientsFromTheDatabase() {
        final var expectedClients = ClientModel.builder().build();
        final var expectedClientsPage = new PageImpl<>(List.of(expectedClients));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var clientDtoResponse = new ClientDtoResponse();
        final var expectedResponse = new PageImpl<>(List.of(clientDtoResponse));


        when(clientService.getClients(null,null,pageable)).thenReturn(expectedClientsPage);

        when(clientMapper.toClientDtoResponse(expectedClients)).thenReturn(clientDtoResponse);

        final var clients = clientController.getAllClients(pageable,null,null);

        verify(clientService).getClients(null,null,pageable);
        verify(clientMapper).toClientDtoResponse(expectedClients);

        verifyNoMoreInteractions(clientService,clientMapper);

        assertEquals(HttpStatus.OK,clients.getStatusCode());
        assertEquals(expectedResponse,clients.getBody().getData());
    }

    @Test
    @DisplayName("Given a CPF get the client that have this CPF")
    void givenVinGetClientWithThisVin() throws ClientNotFoundException {
        final var cpf = "123";
        final var clientModel = ClientModel.builder().build();
        final var clientDtoResponse = new ClientDtoResponse();

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientModel));
        when(clientMapper.toClientDtoResponse(clientModel)).thenReturn(clientDtoResponse);

        final var client = clientController.getClient(cpf);

        verify(clientService).findByCpf(cpf);
        verify(clientMapper).toClientDtoResponse(clientModel);

        verifyNoMoreInteractions(clientService,clientMapper);

        assertEquals(HttpStatus.OK,client.getStatusCode());
        assertEquals(clientDtoResponse,client.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid CPF, throw ClientNotFoundException")
    void givenInvalidVinThrowClientNotFoundException(){
        final var cpf = "123";

        when(clientService.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,() ->clientController.getClient(cpf));
    }

    @Test
    @DisplayName("Given a valid CPF, update the client address info")
    void givenValidVinUpdateTheClientAddressInfo() throws ClientNotFoundException {
        final var cpf = "123";
        final var clientModel = ClientModel.builder().address(AddressModel.builder().postCode("0000").build()).build();
        final var updatedClientModel = ClientModel.builder().address(AddressModel.builder().postCode("1111").build()).build();
        final var clientDtoRequest = new ClientDtoRequest();
        clientDtoRequest.setPostCode("1111");
        final var addressDtoResponse = AddressDtoResponse.builder().postCode("1111").build();
        final var clientDtoResponse = new ClientDtoResponse();
        clientDtoResponse.setAddress(addressDtoResponse);

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientModel));
        when(addressGateway.byPostCode(clientDtoResponse.getAddress().getPostCode())).thenReturn(addressDtoResponse);
        when(clientMapper.toClientModel(clientDtoRequest)).thenReturn(updatedClientModel);
        when(clientService.saveClient(updatedClientModel)).thenReturn(updatedClientModel);
        when(clientMapper.toClientDtoResponse(updatedClientModel)).thenReturn(clientDtoResponse);

        final var updatedClient = clientController.updateClient(cpf,clientDtoRequest);

        verify(clientService).findByCpf(cpf);
        verify(clientMapper).toClientModel(clientDtoRequest);
        verify(clientService).saveClient(updatedClientModel);
        verify(clientMapper).toClientDtoResponse(updatedClientModel);

        verifyNoMoreInteractions(clientService,clientMapper);

        assertEquals(HttpStatus.OK,updatedClient.getStatusCode());
        assertEquals(clientDtoResponse,updatedClient.getBody().getData());
        assertEquals(clientDtoResponse.getAddress(),updatedClient.getBody().getData().getAddress());
    }

    @Test
    @DisplayName("Given a invalid CPF, throw ClientNotFoundException")
    void givenInvalidVinWhenUpdatingThrowClientNotFoundException() {
        final var cpf = "123";
        final var clientDtoRequest = new ClientDtoRequest();

        when(clientService.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,() -> clientController.updateClient(cpf,clientDtoRequest));
    }


    @Test
    @DisplayName("Given a valid CPF, delete the client")
    void givenValidVinDeleteTheClient() throws ClientNotFoundException {
        final var cpf = "123";
        final var clientModel = ClientModel.builder().build();
        final var expectedResponse = new Response<String>();
        expectedResponse.setData("Client with CPF: " + cpf + " was successfully deleted");

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientModel));
        doNothing().when(clientService).deleteClient(cpf);

        final var response = clientController.deleteClient(cpf);

        verify(clientService).findByCpf(cpf);
        verify(clientService).deleteClient(cpf);
        verifyNoMoreInteractions(clientService);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expectedResponse.getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid CPF, throw a ClientNotFoundException")
    void givenInvalidVinWhenDeletingThrowClientNotFoundException(){
        final var cpf = "123";

        when(clientService.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,() -> clientController.deleteClient(cpf));
    }

}