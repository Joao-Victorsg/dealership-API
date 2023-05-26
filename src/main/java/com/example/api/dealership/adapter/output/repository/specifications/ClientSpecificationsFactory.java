package com.example.api.dealership.adapter.output.repository.specifications;

import com.example.api.dealership.core.domain.ClientModel;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecificationsFactory {

    public static Specification<ClientModel> equalCity(String city){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address").get("city"),city));
    }

    public static Specification<ClientModel> equalState(String stateAbbreviation){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address").get("stateAbbreviation"),stateAbbreviation));
    }

}
