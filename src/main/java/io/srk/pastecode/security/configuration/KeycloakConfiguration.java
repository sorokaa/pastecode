package io.srk.pastecode.security.configuration;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfiguration {

    @Bean
    public Keycloak keycloak(KeycloakProperties properties) {
        var adminConsole = properties.getAdminConsole();
        var restClient = new ResteasyClientBuilderImpl()
                .connectionPoolSize(5)
                .build();
        return KeycloakBuilder.builder()
                .serverUrl(properties.getKeycloakUrl())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminConsole.getClientId())
                .clientSecret(adminConsole.getClientSecret())
                .resteasyClient(restClient)
                .build();
    }

    @Bean
    public RealmResource realmResource(KeycloakProperties properties, Keycloak keycloak) {
        return keycloak.realm(properties.getRealm());
    }
}
