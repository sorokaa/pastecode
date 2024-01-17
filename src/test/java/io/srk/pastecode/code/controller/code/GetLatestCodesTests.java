package io.srk.pastecode.code.controller.code;

import io.restassured.common.mapper.TypeRef;
import io.srk.pastecode.ApiTest;
import io.srk.pastecode.code.model.dto.CodeDTO;
import io.srk.pastecode.code.model.entity.Code;
import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.user.model.entity.User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[API] Get latest codes tests")
class GetLatestCodesTests extends ApiTest {

    @AfterEach
    void clear() {
        codeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve 10 latest codes")
    void shouldRetrieve10LatestCodes() {
        // Given
        User user = new User();
        user.setUsername("test@test.com");
        userRepository.save(user);

        List<Code> codes = createRandomCodes(10, user);
        codeRepository.saveAll(codes);

        // When
        List<CodeDTO> actual =
                when()
                    .get("/api/codes")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {
                    });

        // Then
        assertEquals(10, actual.size());
    }

    private List<Code> createRandomCodes(int count, User user) {
        List<Code> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            Code code = new Code();
            code.setOwner(user);
            code.setAvailability(Availability.PUBLIC);
            code.setExpireAt(Instant.now().plus(10, ChronoUnit.DAYS));
            code.setSnippet("test");
            result.add(code);
        }
        return result;
    }
}
