package io.srk.pastecode.user.service;

import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import io.srk.pastecode.security.model.keycloak.dto.CreateKeycloakUserRequest;
import io.srk.pastecode.security.service.KeycloakService;
import io.srk.pastecode.user.model.dto.CreateUserRequest;
import io.srk.pastecode.user.model.entity.User;
import io.srk.pastecode.user.model.mapper.UserMapper;
import io.srk.pastecode.user.repository.UserRepository;
import io.srk.pastecode.user.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UUID userId = SecurityUtil.getCurrentUserId();
        return repository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new BadRequestException(ExceptionCode.USER_ALREADY_EXISTS);
        }
        UUID userId = createKeycloakUser(request);
        User entity = userMapper.toEntity(request);
        entity.setId(userId);
        repository.save(entity);
    }

    private UUID createKeycloakUser(CreateUserRequest request) {
        var keycloakRequest = userMapper.toKeycloakRequest(request);
        var credentials = CreateKeycloakUserRequest.Credentials.builder()
                .type(OAuth2Constants.PASSWORD)
                .value(request.getPassword())
                .temporary(false)
                .build();
        keycloakRequest.setCredentials(List.of(credentials));
        return keycloakService.create(keycloakRequest);
    }
}
