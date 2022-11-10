package com.example.api.dealership.adapter.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ResponseError {

    @NotNull(message="Timestamp cannot be null")
    private LocalDateTime timestamp;

    @NotNull(message="Details cannot be null")
    private String details;

}
