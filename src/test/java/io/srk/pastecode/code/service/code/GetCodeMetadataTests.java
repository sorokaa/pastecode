package io.srk.pastecode.code.service.code;

import io.srk.pastecode.IntegrationTest;
import io.srk.pastecode.code.model.dto.CodeMetadataDTO;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.code.service.CodeService;
import io.srk.pastecode.user.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[INT] Get code metadata tests")
class GetCodeMetadataTests extends IntegrationTest {

    @Autowired
    private CodeService service;

    @AfterEach
    void clear() {
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve secured code")
    void shouldRetrieveSecuredCode() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setSnippet("test");
        entity.setOwner(user);
        entity.setPassword("password");
        entity.setAvailability(Availability.PUBLIC);
        codeRepository.save(entity);

        // When
        CodeMetadataDTO actual = service.getCodeMetadata(entity.getId());

        // Then
        assertTrue(actual.isSecured());
    }

    @Test
    @DisplayName("Should retrieve unsecured code")
    void shouldRetrieveUnsecuredCode() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setSnippet("test");
        entity.setOwner(user);
        entity.setPassword(null);
        entity.setAvailability(Availability.PUBLIC);
        codeRepository.save(entity);

        // When
        CodeMetadataDTO actual = service.getCodeMetadata(entity.getId());

        // Then
        assertFalse(actual.isSecured());
    }
}
