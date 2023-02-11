package com.example.api.dealership.config.rest.token.validator;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {
    private final TokenValidatorImpl tokenValidator;

    @Before("@annotation(com.example.api.dealership.config.rest.token.validator.TokenValidator) && args(..,request)")
    public void before(HttpServletRequest request){

        if(!(request instanceof HttpServletRequest)){
            throw new RuntimeException("request should be HttpServvletRequestType");
        }

        if(!tokenValidator.isValid(request.getHeader("token"))){
            throw new RuntimeException("auth error!!!");
        }
    }
}
