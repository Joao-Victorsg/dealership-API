package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.core.domain.UserModel;
import de.mkammerer.argon2.Argon2Factory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(expression = "java(hashPassword(userDtoRequest))", target = "password")
    UserModel toUserModel(UserDtoRequest userDtoRequest);

    default String hashPassword(UserDtoRequest userDtoRequest){
        var hasher = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32,64);

        return hasher.hash(2,15*1024,1,userDtoRequest.getPassword().toCharArray());
    }

}
