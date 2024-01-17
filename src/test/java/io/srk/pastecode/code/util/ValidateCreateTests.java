package io.srk.pastecode.code.util;

import io.srk.pastecode.UnitTest;
import io.srk.pastecode.code.model.dto.CreateCodeRequest;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[UNIT] Validate code create tests")
class ValidateCreateTests extends UnitTest {

    @Test
    @DisplayName("Should validate request")
    void shouldValidateRequest() {
        // Given
        CreateCodeRequest request = new CreateCodeRequest();
        request.setAvailability(Availability.PUBLIC);
        request.setPassword("password");
        request.setSnippet("test");
        request.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        // When
        assertDoesNotThrow(() -> CodeValidator.validateCreate(request));
    }

    @Test
    @DisplayName("Should not validate blank password")
    void shouldNotValidateBlankPassword() {
        // Given
        CreateCodeRequest request = new CreateCodeRequest();
        request.setAvailability(Availability.PUBLIC);
        request.setPassword(" ");
        request.setSnippet("test");
        request.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> CodeValidator.validateCreate(request)
        );

        assertEquals(ExceptionCode.BLANK_PASSWORD_PROVIDED, actual.getReason());
    }
}