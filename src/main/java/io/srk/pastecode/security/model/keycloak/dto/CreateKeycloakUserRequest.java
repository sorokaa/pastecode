package io.srk.pastecode.security.model.keycloak.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateKeycloakUserRequest {

    private String username;

    private String email;

    private List<Credentials> credentials;

    @Data
    @Builder
    public static class Credentials {

        private String type;

        private String value;

        private boolean temporary;
    }
}
