package io.srk.pastecode.security.model.keycloak.mapper;

import io.srk.pastecode.security.model.keycloak.dto.CreateKeycloakUserRequest;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface KeycloakMapper {

    UserRepresentation toUserRepresentation(CreateKeycloakUserRequest request);
}
