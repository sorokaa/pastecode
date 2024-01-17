package io.srk.pastecode.code.service.code;

import io.srk.pastecode.IntegrationTest;
import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.dto.GetCodeByIdRequest;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.code.service.CodeService;
import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import io.srk.pastecode.user.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[INT] Get code by id tests")
class GetCodeByIdTests extends IntegrationTest {

    @Autowired
    private CodeService service;

    @AfterEach
    void clear() {
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve code without password")
    void shouldRetrieveCodeWithoutPassword() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setSnippet("test");
        entity.setOwner(user);
        entity.setAvailability(Availability.PUBLIC);

        Code save = codeRepository.save(entity);

        // When
        CodeDTO actual = service.getById(save.getId(), new GetCodeByIdRequest());

        // Then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(entity.getSnippet(), actual.getSnippet()),
                () -> assertEquals(entity.getOwner().getId(), actual.getOwnerId()),
                () -> assertEquals(entity.getAvailability(), actual.getAvailability()),
                () -> assertEquals(entity.getExpireAt(), actual.getExpireAt())
        );
    }

    @Test
    @DisplayName("Should retrieve code with password")
    void shouldRetrieveCodeWithPassword() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setSnippet("test");
        entity.setOwner(user);
        entity.setAvailability(Availability.PUBLIC);
        entity.setPassword(new BCryptPasswordEncoder().encode("test-password"));

        Code save = codeRepository.save(entity);

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword("test-password");

        // When
        CodeDTO actual = service.getById(save.getId(), request);

        // Then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(entity.getSnippet(), actual.getSnippet()),
                () -> assertEquals(entity.getOwner().getId(), actual.getOwnerId()),
                () -> assertEquals(entity.getAvailability(), actual.getAvailability()),
                () -> assertEquals(entity.getExpireAt(), actual.getExpireAt())
        );
    }

    @Test
    @DisplayName("Should not retrieve expired code")
    void shouldNotRetrieveExpiredCode() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setExpireAt(Instant.now().minus(10, ChronoUnit.DAYS));
        entity.setOwner(user);
        entity.setAvailability(Availability.PUBLIC);
        entity.setSnippet("test");

        Code saved = codeRepository.save(entity);
        UUID id = saved.getId();

        GetCodeByIdRequest request = new GetCodeByIdRequest();

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> service.getById(id, request)
        );

        // Then
        assertEquals(ExceptionCode.SNIPPET_IS_EXPIRED, actual.getReason());
    }

    @Test
    @DisplayName("Should not retrieve code with wrong password")
    void shouldNotRetrieveCodeWithWrongPassword() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        Code entity = new Code();
        entity.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
        entity.setOwner(user);
        entity.setAvailability(Availability.PUBLIC);
        entity.setPassword("password");
        entity.setSnippet("test");

        Code saved = codeRepository.save(entity);
        UUID id = saved.getId();

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword("wrong-password");

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> service.getById(id, request)
        );

        // Then
        assertEquals(ExceptionCode.WRONG_PASSWORD_PROVIDED, actual.getReason());
    }
}