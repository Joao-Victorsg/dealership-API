package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.core.domain.UserModel;
import de.mkammerer.argon2.Argon2Factory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserModel toUserModel(UserDtoRequest userDtoRequest);

}
