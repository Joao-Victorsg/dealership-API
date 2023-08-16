package com.example.api.dealership.adapter.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public final class ResponseError {

    @NotNull(message="Timestamp cannot be null")
    private final LocalDateTime timestamp;

    @NotNull(message="Details cannot be null")
    private final String details;

    private ResponseError(@NotNull(message = "Timestamp cannot be null") final LocalDateTime timestamp,
                         @NotNull(message = "Details cannot be null") final String details) {
        this.timestamp = timestamp;
        this.details = details;
    }

    public static ResponseError createResponseError(final LocalDateTime timestamp,final String details ){
        return new ResponseError(timestamp,details);
    }
}
