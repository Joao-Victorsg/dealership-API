package com.example.api.dealership.adapter.dtos;

import lombok.Getter;

@Getter
public final class Response<T> {

    private final T data;

    private Response(final T data)
    {
        this.data = data;
    }

    public static <T> Response<T> createResponse(final T data){
        return new Response<>(data);
    }

    public static Response<ResponseError> createResponseWithError(final ResponseError error){
        return new Response<>(error);
    }
}

