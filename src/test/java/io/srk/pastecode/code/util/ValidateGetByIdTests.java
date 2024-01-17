package io.srk.pastecode.code.util;

import io.srk.pastecode.UnitTest;
import io.srk.pastecode.code.model.dto.GetCodeByIdRequest;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[UNIT] Validate get code by id tests")
class ValidateGetByIdTests extends UnitTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Should validate get code by id with password")
    void shouldValidateGetCodeById() {
        // Given
        Code code = new Code();
        code.setPassword(encoder.encode("password"));
        code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword("password");

        // When
        assertDoesNotThrow(
                () -> CodeValidator.validateGetById(code, request)
        );
    }

    @Test
    @DisplayName("Should validate get code by id without password")
    void shouldValidateGetCodeByIdWithoutPassword() {
        // Given
        Code code = new Code();
        code.setPassword(null);
        code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword(null);

        // When
        assertDoesNotThrow(
                () -> CodeValidator.validateGetById(code, request)
        );
    }

    @Test
    @DisplayName("Should  not validate get code by id with wrong password")
    void shouldNotValidateGetCodeByIdWithWrongPassword() {
        // Given
        Code code = new Code();
        code.setPassword(encoder.encode("password"));
        code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword("wrong-password");

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> CodeValidator.validateGetById(code, request)
        );

        // Then
        assertEquals(ExceptionCode.WRONG_PASSWORD_PROVIDED, actual.getReason());
    }

    @Test
    @DisplayName("Should not validate get code by id with null password provided")
    void shouldNotValidateGetCodeByIdWithNullPasswordProvided() {
        // Given
        Code code = new Code();
        code.setPassword(encoder.encode("password"));
        code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword(null);

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> CodeValidator.validateGetById(code, request)
        );

        // Then
        assertEquals(ExceptionCode.WRONG_PASSWORD_PROVIDED, actual.getReason());
    }

    @Test
    @DisplayName("Should not validate get code by id with expired code")
    void shouldNotValidateGetCodeByIdWithExpiredCode() {
        // Given
        Code code = new Code();
        code.setPassword(encoder.encode("password"));
        code.setExpireAt(Instant.now().minus(10, ChronoUnit.DAYS));

        GetCodeByIdRequest request = new GetCodeByIdRequest();
        request.setPassword("password");

        // When
        BadRequestException actual = assertThrows(
                BadRequestException.class,
                () -> CodeValidator.validateGetById(code, request)
        );

        // Then
        assertEquals(ExceptionCode.SNIPPET_IS_EXPIRED, actual.getReason());
    }
}
