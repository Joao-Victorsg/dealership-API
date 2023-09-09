package com.example.api.dealership.adapter.output.repository.specifications;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.jpa.domain.Specification;


public class CarSpecificationsFactory {

    public static Specification<CarModel> betweenValues(final Double initialValue, final Double finalValue){
        return (root, query, builder) ->
                builder.between(root.get("carValue"),initialValue,finalValue);
    }

    public static Specification<CarModel> equalModelYear(final String year){
        return (root, query, builder) ->
                builder.equal(root.get("carModelYear"),year);
    }

    public static Specification<CarModel> equalColor(final String color){
        return (root, query, builder) ->
                builder.equal(root.get("carColor"), color);
    }

}
