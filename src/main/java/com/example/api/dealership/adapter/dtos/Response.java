package com.example.api.dealership.adapter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private T data;

    private Object errors;

    public void addErrorMsgToResponse(String msgError){
        this.errors = ResponseError.builder()
                .details(msgError)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
