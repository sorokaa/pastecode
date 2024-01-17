package io.srk.pastecode.code.service.code;

import io.srk.pastecode.IntegrationTest;
import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.code.service.CodeService;
import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import io.srk.pastecode.user.model.entity.User;
import io.srk.pastecode.user.util.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[INT] Create code tests")
class CreateCodeTests extends IntegrationTest {

    @Autowired
    private CodeService service;

    private MockedStatic<SecurityUtil> utilities;
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test@test.com");
        userRepository.save(user);
        MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class);
        utilities.when(SecurityUtil::getCurrentUserId).thenReturn(user.getId());
        this.utilities = utilities;
    }

    @AfterEach
    void clear() {
        utilities.close();
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create code with password")
    void shouldCreateCodeWithPassword() {
        // Given
        CreateCodeRequest request = new CreateCodeRequest();
        request.setPassword("test");
        request.setSnippet("snippet");
        request.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
        request.setAvailability(Availability.PUBLIC);

        // When
        CodeDTO actual = service.create(request);

        // Then
        assertNotNull(actual);
        assertTrue(codeRepository.existsById(actual.getId()));
        Optional<Code> databaseEntityOpt = codeRepository.findById(actual.getId());
        assertTrue(databaseEntityOpt.isPresent());
        Code entity = databaseEntityOpt.get();
        assertAll(
                () -> assertTrue(ENCODER.matches(request.getPassword(), entity.getPassword())),
                () -> assertEquals(request.getSnippet(), entity.getSnippet()),
                () -> assertEquals(request.getAvailability(), entity.getAvailability())
        );
    }

    @Test
    @DisplayName("Should create code without password")
    void shouldCreateCodeWithoutPassword() {
        // Given
        CreateCodeRequest request = new CreateCodeRequest();
        request.setPassword(null);
        request.setSnippet("snippet");
        request.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
        request.setAvailability(Availability.PUBLIC);

        // When
        CodeDTO actual = service.create(request);

        // Then
        assertNotNull(actual);
        assertTrue(codeRepository.existsById(actual.getId()));
        Optional<Code> databaseEntityOpt = codeRepository.findById(actual.getId());
        assertTrue(databaseEntityOpt.isPresent());
        Code entity = databaseEntityOpt.get();
        assertAll(
                () -> assertNull(entity.getPassword()),
                () -> assertEquals(request.getSnippet(), entity.getSnippet()),
                () -> assertEquals(request.getAvailability(), entity.getAvailability())
        );
    }

    @Test
    @DisplayName("Should not create code with blank password")
    void shouldNotCreateCodeWithBlankPassword() {
        // Given
        CreateCodeRequest request = new CreateCodeRequest();
        request.setPassword("     ");
        request.setSnippet("snippet");
        request.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
        request.setAvailability(Availability.PUBLIC);

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> service.create(request)
        );

        // Then
        assertEquals(ExceptionCode.BLANK_PASSWORD_PROVIDED, actual.getReason());
    }
}
