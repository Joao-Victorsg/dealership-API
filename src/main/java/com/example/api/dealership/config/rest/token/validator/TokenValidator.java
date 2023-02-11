package com.example.api.dealership.config.rest.token.validator;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//@Constraint(validatedBy = TokenValidatorImpl.class)
public @interface TokenValidator {

    public boolean isValid() default true;

}
