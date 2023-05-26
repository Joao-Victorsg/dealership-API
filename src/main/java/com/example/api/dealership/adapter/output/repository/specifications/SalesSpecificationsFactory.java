package com.example.api.dealership.adapter.output.repository.specifications;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SalesSpecificationsFactory {

    public static Specification<SalesModel> betweenDates(LocalDate initialDate, LocalDate finalDate){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("registrationDate"),initialDate,finalDate));
    }


}
