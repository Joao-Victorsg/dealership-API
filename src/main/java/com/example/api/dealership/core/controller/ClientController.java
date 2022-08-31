package com.example.api.dealership.core.controller;


import com.example.api.dealership.core.adapter.client.ClientRepositoryAdapter;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.dtos.client.ClientDtoRequest;
import com.example.api.dealership.core.dtos.client.ClientDtoResponse;
import com.example.api.dealership.core.mapper.ClientMapper;
import com.example.api.dealership.core.repository.ClientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepositoryAdapter clientRepositoryAdapter;

    private final ClientMapper clientMapper;

    @Operation(summary="Return a page of clients")
    @ApiResponse(responseCode = "200",description = "Return a list of clients")
    @GetMapping(path= "/client",produces = "application/json")
    private ResponseEntity<Page<ClientDtoResponse>> getAllClients(@PageableDefault(page = 0,size = 10, sort ="id",
            direction = Sort.Direction.ASC) Pageable pageable){

        var clients = clientRepositoryAdapter.getClients(pageable);

        var response = new PageImpl<>(clients.stream().map(client -> clientMapper.toClientDtoResponse(client)).collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @Operation(summary="Return one client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The client was returned with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a client with the CPF that was informed.")
    })
    @GetMapping(path = "/client/{cpf}", produces = "application/json")
    private ResponseEntity<Object> getClient(@PathVariable(value = "cpf") String cpf){
        var cliente = clientRepositoryAdapter.findByCpf(cpf);

        if(cliente.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toClientDtoResponse(cliente.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A client with this CPF doesn't exists");
    }


    @Operation(summary = "Save a client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The client was created with success"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the client")
    })
    @PostMapping(path = "/client")
    private ResponseEntity<Object> saveClient(@RequestBody @Valid ClientDtoRequest request){
        var cliente = clientRepositoryAdapter.findByCpf(request.getCpf());

        if(cliente.isEmpty()){
            var clientModel = clientRepositoryAdapter.saveClient(clientMapper.toClientModel(request));
            log.info("Creating client in the database: " + clientModel);
            return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toClientDtoResponse(clientModel));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("A client with this CPF already exists");
    }

    @Operation(summary = "Update a client name or/and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The client was update with success."),
            @ApiResponse(responseCode = "404",description = "There wasn't a client with the VIN that was informed.")
    })
    @PutMapping(path = "/client/{cpf}", produces = "application/json")
    private ResponseEntity<Object> updateClient(@PathVariable(value = "cpf") String cpf, @RequestBody ClientDtoRequest request){
        var cliente = clientRepositoryAdapter.findByCpf(cpf);

        if(cliente.isPresent()){
            var clientModel = cliente.get();
            var clientModelUpdate = clientMapper.toClientModel(request);

            clientModel.setName(clientModelUpdate.getName());
            clientModel.setAddress(clientModelUpdate.getAddress());

            var response = clientRepositoryAdapter.saveClient(clientModel);
            log.info("Updating client: " + response);
            return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toClientDtoResponse(response));

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A client with this CPF was not found");
    }

    @Operation(summary = "Delete a client passing the CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The client was deleted with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a client with the CPF that was informed in the database.")
    })
    @DeleteMapping(path = "/client/{cpf}", produces = "application/json")
    public ResponseEntity<Object> deleteClient(@PathVariable(value = "cpf") String cpf){

        var  clientModelOptional = clientRepositoryAdapter.findByCpf(cpf);

        if(clientModelOptional.isPresent()){
            clientRepositoryAdapter.deleteClient(cpf);
            log.info("Deleted client with CPF: " + cpf);
            return ResponseEntity.status(HttpStatus.OK).body("Client deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found.");
    }

}
