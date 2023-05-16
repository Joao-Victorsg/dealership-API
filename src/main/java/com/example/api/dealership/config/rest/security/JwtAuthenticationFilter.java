package com.example.api.dealership.config.rest.security;

import com.example.api.dealership.adapter.service.security.JwtService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final var authHeader = request.getHeader(AUTHORIZATION);

        if(isNotAValidToken(authHeader)){
            filterChain.doFilter(request,response);
            return;
        }

        final var jwt = getToken(authHeader);
        final var username = jwtService.extractUsername(jwt);

        if(username != null && isNotLoggedYet()){
            final var userDetails = this.userDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(jwt,userDetails)){

                final var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Updating the context to set who ir authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request,response);
    }

    private boolean isNotAValidToken(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer");
    }

    private boolean isNotLoggedYet(){
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String getToken(String authHeader){
        return authHeader.split(" ")[1];
    }
}
