package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.adapter.dtos.client.ClientDtoUpdateRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {


    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @Test
    @DisplayName("Given a valid client request, save it in the database")
    void givenValidClientRequestSaveItInTheDatabase() throws DuplicatedInfoException {
        final var clientDtoRequest = ClientDtoRequest.builder()
                .cpf("123")
                .address(AddressDtoRequest.builder()
                        .build())
                .build();
        final var clientModel = ClientModel.builder()
                .cpf("123")
                .address(AddressModel.builder()
                        .build())
                .build();
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/clients/" + clientModel.getCpf()))
                .body(Response.createResponse(ClientDtoResponse.builder()
                                .cpf("123")
                        .address(AddressDtoResponse.builder()
                                .build())
                        .build()));

        when(clientService.saveClient(any(ClientModel.class))).thenReturn(clientModel);

        final var response = clientController.saveClient(clientDtoRequest);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(expectedResponse.getBody().getData().cpf(),response.getBody().getData().cpf());
    }

    @Test
    @DisplayName("Given a client request with a CPF that already exists, throw DuplicatedInfoException ")
    void givenClientRequestWithVinThatAlreadyExistsThrowDuplicatedInfoException() throws DuplicatedInfoException {
        final var clientDto = ClientDtoRequest.builder().address(AddressDtoRequest.builder().build()).build();

        doThrow(DuplicatedInfoException.class).when(clientService).saveClient(any(ClientModel.class));

        assertThrows(DuplicatedInfoException.class, () -> clientController.saveClient(clientDto));
    }

    @Test
    @DisplayName("Get a page of clients from the database")
    void getAPageOfClientsFromTheDatabase() {
        final var expectedClients = ClientModel.builder().address(AddressModel.builder().build()).build();
        final var expectedClientsPage = new PageImpl<>(List.of(expectedClients));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var clientDtoResponse = ClientDtoResponse.builder().build();
        final var expectedResponse = new PageImpl<>(List.of(clientDtoResponse));

        when(clientService.getClients(null,null,pageable)).thenReturn(expectedClientsPage);

        final var clients = clientController.getAllClients(pageable,null,null);

        assertEquals(HttpStatus.OK,clients.getStatusCode());
        assertEquals(expectedResponse.getTotalElements(),clients.getBody().getData().getTotalElements());
    }

    @Test
    @DisplayName("Given a CPF get the client that have this CPF")
    void givenVinGetClientWithThisVin(){
        final var cpf = "123";
        final var clientModel = ClientModel.builder().cpf("123").address(AddressModel.builder().build()).build();
        final var clientDtoResponse = ClientDtoResponse.builder().cpf("123").address(AddressDtoResponse.builder().build()).build();

        when(clientService.findByCpf(cpf)).thenReturn(Optional.of(clientModel));

        assertDoesNotThrow(() -> {
                    final var response = clientController.getClient(cpf);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(clientDtoResponse.cpf(), response.getBody().getData().cpf());
                }
        );
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
        final var request = ClientDtoUpdateRequest.builder().postCode("0000").build();
        final var clientModel = ClientModel.builder().address(AddressModel.builder().postCode("0000").build()).build();
        final var expectedResponse = ResponseEntity.status(HttpStatus.OK)
                .body(Response.createResponse(ClientDtoResponse.builder()
                        .address(AddressDtoResponse.builder()
                                .postCode("0000")
                                .build())
                        .build()));

        when(clientService.updateClient(cpf,request)).thenReturn(clientModel);

        assertDoesNotThrow(() -> {
                    final var updatedClient = clientController.updateClient(cpf, request);
                    assertEquals(HttpStatus.OK, updatedClient.getStatusCode());
                    assertEquals(expectedResponse.getBody().getData().address().postCode(),
                            updatedClient.getBody().getData().address().postCode());
                }
        );
    }

    @Test
    @DisplayName("Given a invalid CPF, throw ClientNotFoundException")
    void givenInvalidVinWhenUpdatingThrowClientNotFoundException() throws ClientNotFoundException {
        final var cpf = "123";
        final var clientDtoRequest = ClientDtoUpdateRequest.builder().build();

        doThrow(ClientNotFoundException.class).when(clientService).updateClient(cpf,clientDtoRequest);

        assertThrows(ClientNotFoundException.class,() -> clientController.updateClient(cpf,clientDtoRequest));
    }


    @Test
    @DisplayName("Given a valid CPF, delete the client")
    void givenValidVinDeleteTheClient() throws ClientNotFoundException {
        final var cpf = "123";
        final var expectedResponse = ResponseEntity.status(HttpStatus.OK).body(Response.
                createResponse("Client with CPF: " + cpf + " was successfully deleted"));

        doNothing().when(clientService).deleteClient(cpf);

        assertDoesNotThrow(() -> {
                    final var response = clientController.deleteClient(cpf);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(expectedResponse.getBody().getData(), response.getBody().getData());
                }
        );
    }

    @Test
    @DisplayName("Given a invalid CPF, throw a ClientNotFoundException")
    void givenInvalidVinWhenDeletingThrowClientNotFoundException() throws ClientNotFoundException {
        final var cpf = "123";

        doThrow(ClientNotFoundException.class).when(clientService).deleteClient(cpf);

        assertThrows(ClientNotFoundException.class,() -> clientController.deleteClient(cpf));
    }

}
