package ru.cinimex.taskmanagerservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cinimex.taskmanagerservice.domain.UserEntity;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;
import ru.cinimex.taskmanagerservice.service.UserService;

import java.util.Date;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface UserMapper{

    UserEntity userDtoToEntity(RegisterRequest registerRequest);

    RegisterRequest userEntityToDto(UserEntity userEntity);

    @AfterMapping
    default void fillAdditionalFields(@MappingTarget UserEntity userEntity) {
        userEntity.setCreatedAt(new Date());
        userEntity.setUpdatedAt(new Date());
        userEntity.setRole("ROLE_USER");

    }
}
