package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.core.domain.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserModel toUserModel(UserDtoRequest userDtoRequest);

}

