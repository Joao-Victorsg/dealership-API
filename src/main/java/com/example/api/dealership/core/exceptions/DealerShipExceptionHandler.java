package com.example.api.dealership.core.exceptions;

import com.example.api.dealership.adapter.dtos.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DealerShipExceptionHandler<T> {

    @ExceptionHandler(value = {CarNotFoundException.class})
    protected ResponseEntity<Response<T>> handleCarNotFoundException(CarNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {DuplicatedInfoException.class})
    protected ResponseEntity<Response<T>> handleDuplicatedInfoException(DuplicatedInfoException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {ClientNotFoundException.class})
    protected ResponseEntity<Response<T>> handleClientNotFoundException(ClientNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {SaleNotFoundException.class})
    protected ResponseEntity<Response<T>> handleSaleNotFoundException(SaleNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {CarAlreadySoldException.class})
    protected ResponseEntity<Response<T>> handleCarAlreadySoldException(CarAlreadySoldException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private Response<T> buildResponseException(Exception ex){
        Response<T> response = new Response<>();
        response.addErrorMsgToResponse(ex.getLocalizedMessage());

        return response;
    }
}
