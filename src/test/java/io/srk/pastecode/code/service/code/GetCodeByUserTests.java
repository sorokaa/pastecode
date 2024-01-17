package io.srk.pastecode.code.service.code;

import io.srk.pastecode.IntegrationTest;
import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.code.service.CodeService;
import io.srk.pastecode.user.model.entity.User;
import io.srk.pastecode.user.util.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[INT] Get codes by user tests")
class GetCodeByUserTests extends IntegrationTest {

    @Autowired
    private CodeService service;

    private User user;
    private MockedStatic<SecurityUtil> utilities;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test@test.com");
        userRepository.save(user);
        this.user = user;
        MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class);
        utilities.when(SecurityUtil::getCurrentUserId).thenReturn(user.getId());
        this.utilities = utilities;
    }

    @AfterEach
    void clear() {
        this.utilities.close();
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve only user's codes")
    void shouldRetrieveCodeWithoutPassword() {
        // Given
        User wrongUser = new User();
        wrongUser.setId(UUID.randomUUID());
        wrongUser.setUsername("test-2@test.com");
        userRepository.save(wrongUser);

        List<Code> codes = createRandomCodes(10, user);
        codes.get(0).setOwner(wrongUser);
        codeRepository.saveAll(codes);

        // When
        Page<CodeDTO> actual = service.getByUser(null);

        // Then
        assertTrue(actual.stream().allMatch(code -> code.getOwnerId().equals(user.getId())));
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
