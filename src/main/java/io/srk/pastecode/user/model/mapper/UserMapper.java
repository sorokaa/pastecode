package io.srk.pastecode.user.model.mapper;

import io.srk.pastecode.security.model.keycloak.dto.CreateKeycloakUserRequest;
import io.srk.pastecode.user.model.dto.CreateUserRequest;
import io.srk.pastecode.user.model.entity.User;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    CreateKeycloakUserRequest toKeycloakRequest(CreateUserRequest request);

    User toEntity(CreateUserRequest request);
}
