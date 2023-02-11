package com.example.api.dealership.config.rest.token.validator;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class TokenValidatorImpl{
    public boolean isValid(String token) {
        try {
            return new Token(token).isValid();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao validar o token");
        }
    }
}
