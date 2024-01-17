package io.srk.pastecode.security.service;

import io.srk.pastecode.security.model.keycloak.dto.CreateKeycloakUserRequest;
import io.srk.pastecode.security.model.keycloak.mapper.KeycloakMapper;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RealmResource realm;
    private final KeycloakMapper mapper;

    public UUID create(CreateKeycloakUserRequest request) {
        var userRepresentation = mapper.toUserRepresentation(request);
        userRepresentation.setEnabled(true);
        Response response = realm.users().create(userRepresentation);
        String userId = CreatedResponseUtil.getCreatedId(response);
        return UUID.fromString(userId);
    }
}
