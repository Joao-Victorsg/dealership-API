package com.example.api.dealership.config.rest.handlers.exceptions;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler<T> extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<T>> handleIllegalArgumentException(
            IllegalArgumentException ex
    ){
        final var response = buildResponseException(ex);

        return ResponseEntity.badRequest().body(response);
    }

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

    @ExceptionHandler(value = {ClientNotHaveRegisteredAddressException.class})
    protected ResponseEntity<Response<T>> handleClientNotHaveRegisteredAddressException(ClientNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
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

    @ExceptionHandler(value = {ExpiredJwtException.class})
    protected ResponseEntity<Response<T>> handleExpiredJwtException(ExpiredJwtException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<Response<T>> handleUsernameNotFoundException(UsernameNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    private Response<T> buildResponseException(Exception ex){
        Response<T> response = new Response<>();
        response.addErrorMsgToResponse(ex.getLocalizedMessage());

        return response;
    }

}