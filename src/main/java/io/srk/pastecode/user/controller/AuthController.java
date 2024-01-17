package io.srk.pastecode.user.controller;

import io.srk.pastecode.user.model.dto.CreateUserRequest;
import io.srk.pastecode.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Creates user")
    @PostMapping
    public void createUser(@RequestBody CreateUserRequest request) {
        log.debug("API request to create user. Request: {}", request);
        userService.createUser(request);
    }
}
