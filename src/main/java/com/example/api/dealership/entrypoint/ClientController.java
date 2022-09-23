package com.example.api.dealership.entrypoint;


import com.example.api.dealership.core.adapter.client.ClientRepositoryAdapter;
import com.example.api.dealership.core.dtos.client.ClientDtoRequest;
import com.example.api.dealership.core.dtos.client.ClientDtoResponse;
import com.example.api.dealership.core.mapper.ClientMapper;
import com.example.api.dealership.core.port.SearchAddressPort;
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
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepositoryAdapter clientRepositoryAdapter;

    private final ClientMapper clientMapper;

    private final SearchAddressPort restTemplatePort;


    @Operation(summary="Return a page of clients")
    @ApiResponse(responseCode = "200",description = "Return a list of clients")
    @GetMapping(path= "/clients",produces = "application/json")
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
    @GetMapping(path = "/clients/{cpf}", produces = "application/json")
    private ResponseEntity<Object> getClient(@PathVariable(value = "cpf") String cpf){
        var cliente = clientRepositoryAdapter.findByCpf(cpf);

        if(cliente.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(clientMapper.toClientDtoResponse(cliente.get()));
        }

        //Deveria ser uma mensagem de exceção e não de corpo
        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Save a client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The client was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the client"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service ins unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/clients")
    private ResponseEntity<Object> saveClient(@RequestBody @Valid ClientDtoRequest request){
        var cliente = clientRepositoryAdapter.findByCpf(request.getCpf());

        if(cliente.isEmpty()){

            var clientAddress = restTemplatePort.searchAddressByPostCode(request.getPostCode());

            BeanUtils.copyProperties(clientAddress,request);

            var clientModel = clientRepositoryAdapter.saveClient(clientMapper.toClientModel(request));
            log.info("Creating client in the database: " + clientModel);
            //201 é pra sincrono
            //202 é pra assincrono
            return ResponseEntity.created(URI.create("/v1/dealership/client/" + clientModel.getCpf()))
                    .body(clientMapper.toClientDtoResponse(clientModel));
        }
        // O comum seria ter um DTO pra devolver nos casos de erro. Esse DTO poderia ter o código, etc.
        return ResponseEntity.status(HttpStatus.CONFLICT).body("A client with this CPF already exists");
    }



    @Operation(summary = "Update a client name or/and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The client was update with success."),
            @ApiResponse(responseCode = "404",description = "There wasn't a client with the VIN that was informed.")
    })
    @PutMapping(path = "/clients/{cpf}", produces = "application/json")
    private ResponseEntity<Object> updateClient(@PathVariable(value = "cpf") String cpf, @RequestBody ClientDtoRequest request){
        var client = clientRepositoryAdapter.findByCpf(cpf);

        if(client.isPresent()){
            var clientModel = client.get();

            var clientAddress = restTemplatePort.searchAddressByPostCode(request.getPostCode());
            BeanUtils.copyProperties(clientAddress,request);

            var clientModelUpdate = clientMapper.toClientModel(request);

            clientModelUpdate.setId(clientModel.getId());
            clientModelUpdate.setCpf(clientModel.getCpf());
            clientModelUpdate.getAddress().setId(clientModel.getAddress().getId());
            
            var response = clientRepositoryAdapter.saveClient(clientModelUpdate);
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
    @DeleteMapping(path = "/clients/{cpf}", produces = "application/json")
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
