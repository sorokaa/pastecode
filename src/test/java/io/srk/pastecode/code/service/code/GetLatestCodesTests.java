package io.srk.pastecode.code.service.code;

import io.srk.pastecode.IntegrationTest;
import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.code.service.CodeService;
import io.srk.pastecode.user.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[INT] Get latest codes tests")
class GetLatestCodesTests extends IntegrationTest {

    @Autowired
    private CodeService service;

    @AfterEach
    void clear() {
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve only 10 public records")
    void shouldRetrieveCodeWithoutPassword() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test@test.com");
        userRepository.save(user);

        List<Code> codes = createRandomCodes(10, user);
        codeRepository.saveAll(codes);

        // When
        List<CodeDTO> actual = service.getLatestCodes();

        // Then
        assertEquals(10, actual.size());
    }

    @Test
    @DisplayName("Should not retrieve unlisted codes")
    void shouldRetrieveCodeWithPassword() {
        // Given
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test@test.com");
        userRepository.save(user);

        List<Code> codes = createRandomCodes(10, user);
        codes.get(0).setAvailability(Availability.UNLISTED);
        codeRepository.saveAll(codes);

        // When
        List<CodeDTO> actual = service.getLatestCodes();

        // Then
        assertEquals(9, actual.size());
        assertTrue(actual.stream().noneMatch(code -> code.getAvailability().equals(Availability.UNLISTED)));
    }

    @Test
    @DisplayName("Should not retrieve expired codes")
    void shouldNotRetrieveExpiredCodes() {
        // Given
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test@test.com");
        userRepository.save(user);

        List<Code> codes = createRandomCodes(5, user);
        codes.get(0).setExpireAt(Instant.now().minus(10, ChronoUnit.DAYS));
        codeRepository.saveAll(codes);

        // When
        List<CodeDTO> actual = service.getLatestCodes();

        // Then
        assertEquals(4, actual.size());
        assertTrue(actual.stream().noneMatch(code -> code.getExpireAt().isBefore(Instant.now())));
    }

    private List<Code> createRandomCodes(int count, User owner) {
        List<Code> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            Code code = new Code();
            code.setOwner(owner);
            code.setCreated(Instant.now());
            code.setSnippet("test-" + i);
            code.setAvailability(Availability.PUBLIC);
            code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
            result.add(code);
        }
        return result;
    }
}