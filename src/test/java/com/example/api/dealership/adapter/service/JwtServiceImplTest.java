/*
package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.service.security.JwtService;
import com.example.api.dealership.adapter.service.security.impl.JwtServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp(){
        this.jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY","4D6251655468576D5A7133743677397A24432646294A404E635266556A586E32");
    }

    @Test
    @DisplayName("Given a valid userdetails return a token")
    void givenUserDetailsReturnAToken(){
        final var userDetails = getUserDetails(getUserModel());
        final var expectedToken = generateToken();

        final var token = jwtService.generateToken(userDetails);

        assertEquals(expectedToken,token);
    }

    @Test
    @DisplayName("Given token, extract the username")
    void givenTokenExtractTheUsername(){
        final var token = generateToken();
        final var expectedUsername = getUserModel().getUsername();

        final var username = jwtService.extractUsername(token);

        assertEquals(username,expectedUsername);
    }

    @Test
    @DisplayName("Given a valid token, verify if it's valid")
    void givenTokenVerifyIfValid(){
        final var token = generateToken();
        final var userDetails = getUserDetails(getUserModel());

        final var isValid = jwtService.isTokenValid(token,userDetails);

        assertTrue(isValid);
    }
    @Test
    @DisplayName("Given a invalid token, with wrong username, verify if it's valid")
    void givenInvalidTokenVerifyIfValid(){
        final var token = generateInvalidToken("wrongUsername",new Date(System.currentTimeMillis() + 1000));
        final var userDetails = getUserDetails(getUserModel());

        final var isInvalid = jwtService.isTokenValid(token,userDetails);

        assertFalse(isInvalid);
    }

    @Test
    @DisplayName("Given a expired token verify if it's valid")
    void givenExpiredTokenVerifyIfValid(){
        final var token = generateInvalidToken(getUserModel().getUsername(),new Date(System.currentTimeMillis() - 1000));
        final var userDetails = getUserDetails(getUserModel());
        assertThrows(ExpiredJwtException.class,() -> jwtService.isTokenValid(token,userDetails));
    }

    @Test
    @DisplayName("Given a not expired token verify if it's valid")
    void givenNotExpiredTokenVerifyIfValid(){
        final var token = generateInvalidToken(getUserModel().getUsername(),new Date(System.currentTimeMillis() + 10000));
        final var userDetails = getUserDetails(getUserModel());

        final var isValid = jwtService.isTokenValid(token,userDetails);

        assertTrue(isValid);
    }

    private String generateInvalidToken(String username, Date expiration) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256,"4D6251655468576D5A7133743677397A24432646294A404E635266556A586E32")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .compact();
    }


    private String generateToken() {
        return Jwts.builder()
                .setSubject(getUserModel().getUsername())
                .signWith(SignatureAlgorithm.HS256,"4D6251655468576D5A7133743677397A24432646294A404E635266556A586E32")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .compact();
    }

    private UserModel getUserModel(){
        return UserModel.builder()
                .username("username")
                .password("123456")
                .isAdmin(false)
                .id("id")
                .build();
    }

    private UserDetails getUserDetails(UserModel userModel){
        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .authorities("ROLE_USER")
                .build();
    }

}*/
