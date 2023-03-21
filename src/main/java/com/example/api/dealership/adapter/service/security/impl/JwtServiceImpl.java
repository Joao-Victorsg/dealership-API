package com.example.api.dealership.adapter.service.security.impl;

import com.example.api.dealership.adapter.service.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.expiration}")
    private String expirationTime;

    //@Value("${security.jwt.key}")
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Override
    public String generateToken(UserDetails userDetail) {
        var dateTimeExpiration = LocalDateTime.now().plusMinutes(Long.parseLong(expirationTime));
        var expirationDate = Date.from(dateTimeExpiration.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,"EQtEvJZoBSv9maVfO74yyYyS3HjjDDVsEo17IbHs67lYx8mwYCj3uqH0WTJUWeoXVeLPf3AFcm3odXrawasdasfvcxbtgfyht4r32132135gbvchgfdhgfd")
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        var claims = getClaims(token);
        var expirationDate = claims.getExpiration();
        var expirationDateTime = expirationDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        return expirationDateTime.isAfter(LocalDateTime.now());
    }

    //TODO: Validar se esse cara tá funcionando corretamente
    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey("EQtEvJZoBSv9maVfO74yyYyS3HjjDDVsEo17IbHs67lYx8mwYCj3uqH0WTJUWeoXVeLPf3AFcm3odXrawasdasfvcxbtgfyht4r32132135gbvchgfdhgfd")
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

}
