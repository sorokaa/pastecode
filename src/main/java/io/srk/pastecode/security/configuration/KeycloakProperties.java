package io.srk.pastecode.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String keycloakUrl;

    private String realm;

    private AdminCliProperties adminConsole;

    @Data
    public static class AdminCliProperties {

        private String clientId;

        private String clientSecret;
    }
}
