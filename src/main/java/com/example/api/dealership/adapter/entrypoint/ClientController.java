package com.example.api.dealership.adapter.entrypoint;


import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoResponse;
import com.example.api.dealership.adapter.mapper.ClientMapper;
import com.example.api.dealership.adapter.output.gateway.SearchAddressGateway;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/dealership")
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    private final SearchAddressGateway restTemplatePort;


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
    private ResponseEntity<Response<Page<ClientDtoResponse>>> getAllClients(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable) {

        var response = new Response<Page<ClientDtoResponse>>();

        var clients = clientService.getClients(pageable);

        response.setData(new PageImpl<>(
                clients.
                        stream().
                        map(client -> clientMapper.toClientDtoResponse(client)).
                        collect(Collectors.toList()))
        );

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
    private ResponseEntity<Response<ClientDtoResponse>> getClient(@PathVariable(value = "cpf") String cpf) throws ClientNotFoundException {

        var response = new Response<ClientDtoResponse>();

        var cliente = clientService.findByCpf(cpf);

        if (cliente.isPresent()) {
            response.setData(clientMapper.toClientDtoResponse(cliente.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new ClientNotFoundException("There isn't a client with this CPF");
    }


    @Operation(summary = "Save a client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The client was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the client"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/clients")
    private ResponseEntity<Response<ClientDtoResponse>> saveClient(@RequestBody @Valid ClientDtoRequest request) throws DuplicatedInfoException {
        var response = new Response<ClientDtoResponse>();

        var cliente = clientService.findByCpf(request.getCpf());

        if (cliente.isEmpty()) {

            var clientAddress = restTemplatePort.searchAddressByPostCode(request.getPostCode());

            BeanUtils.copyProperties(clientAddress, request);

            var clientModel = clientService.saveClient(clientMapper.toClientModel(request));
            log.info("Creating client in the database: " + clientModel);

            response.setData(clientMapper.toClientDtoResponse(clientModel));

            return ResponseEntity.created(URI.create("/v1/dealership/client/" + clientModel.getCpf()))
                    .body(response);
        }
        throw new DuplicatedInfoException("A client with this CPF already exists");
    }

    @Operation(summary = "Update a client name or/and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was update with success."),
            @ApiResponse(responseCode = "404", description = "There wasn't a client with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PutMapping(path = "/clients/{cpf}", produces = "application/json")
    private ResponseEntity<Response<ClientDtoResponse>> updateClient(@PathVariable(value = "cpf") String cpf, @RequestBody ClientDtoRequest request) throws ClientNotFoundException {
        var response = new Response<ClientDtoResponse>();

        var client = clientService.findByCpf(cpf);

        if (client.isPresent()) {
            var clientModel = client.get();

            var clientAddress = restTemplatePort.searchAddressByPostCode(request.getPostCode());
            BeanUtils.copyProperties(clientAddress, request);

            var clientModelUpdate = clientMapper.toClientModel(request);

            clientModelUpdate.setId(clientModel.getId());
            clientModelUpdate.setCpf(clientModel.getCpf());
            clientModelUpdate.getAddress().setId(clientModel.getAddress().getId());

            clientModel = clientService.saveClient(clientModelUpdate);

            response.setData(clientMapper.toClientDtoResponse(clientModel));

            log.info("Updating client: " + response.getData());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new ClientNotFoundException("A client with this CPF was not found");
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
    public ResponseEntity<Response<String>> deleteClient(@PathVariable(value = "cpf") String cpf) throws ClientNotFoundException {

        var response = new Response<String>();

        var clientModelOptional = clientService.findByCpf(cpf);

        if (clientModelOptional.isPresent()) {
            clientService.deleteClient(cpf);

            log.info("Client with CPF: " + cpf + "was successfully deleted");

            response.setData("Client with CPF: " + cpf + " was successfully deleted");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        throw new ClientNotFoundException("There isn't a client with this CPF");
    }
}
