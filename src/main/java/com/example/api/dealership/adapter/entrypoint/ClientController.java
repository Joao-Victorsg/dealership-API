package com.example.api.dealership.adapter.entrypoint;


import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.adapter.dtos.client.ClientDtoUpdateRequest;
import com.example.api.dealership.adapter.mapper.ClientMapper;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.example.api.dealership.adapter.mapper.ClientMapper.toClientDtoResponse;
import static com.example.api.dealership.adapter.mapper.ClientMapper.toClientModel;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class ClientController {

    private final ClientService clientService;

    private final MeterRegistry meterRegistry;

    @Operation(summary = "Return a page of clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of clients"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/clients", produces = "application/json")
    public ResponseEntity<Response<PageImpl<ClientDtoResponse>>> getAllClients(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) final String city,
                                                                               @RequestParam(required = false) final String state) {

        final var clients = clientService.getClients(city,state,pageable);

        final var clientsDtoList = clients.
                stream().
                map(ClientMapper::toClientDtoResponse).
                toList();

        final var response = Response.createResponse(
                new PageImpl<>(clientsDtoList,clients.getPageable(), clientsDtoList.size()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Return one client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the CPF that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<ClientDtoResponse>> getClient(@PathVariable(value = "cpf") final String cpf) throws ClientNotFoundException {
        final var client = clientService.findByCpf(cpf);

        if (client.isEmpty())
            throw new ClientNotFoundException("There isn't a client with this CPF");

        final var response = Response
                .createResponse(toClientDtoResponse(client.get()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Save a client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The client was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the client"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/clients")
    public ResponseEntity<Response<ClientDtoResponse>> saveClient(@RequestBody @Valid final ClientDtoRequest request) throws DuplicatedInfoException {
        final var client = toClientModel(request);

        final var savedClient = clientService.saveClient(client);

        log.info("Creating client in the database: " + savedClient);

        meterRegistry.counter("number_of_clients").increment();

        final var response = Response.createResponse(toClientDtoResponse(savedClient));

        return ResponseEntity.created(URI.create("/v1/dealership/client/" + savedClient.getCpf()))
                .body(response);
    }

    @Operation(summary = "Update a client name or/and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was update with success."),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unavailable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PutMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<ClientDtoResponse>> updateClient(@PathVariable(value = "cpf") final String cpf, @RequestBody final ClientDtoUpdateRequest request) throws ClientNotFoundException {
        final var updatedClient = clientService.updateClient(cpf,request);

        final var response = Response.createResponse(toClientDtoResponse(updatedClient));

        log.info("Updating client: " + response.getData());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a client passing the CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was deleted with success"),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the CPF that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/clients/{cpf}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteClient(@PathVariable(value = "cpf") final String cpf) throws ClientNotFoundException {

        clientService.deleteClient(cpf);

        log.info("Client with CPF: " + cpf + "was successfully deleted");

        final var response = Response.createResponse("Client with CPF: " + cpf + " was successfully deleted");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
