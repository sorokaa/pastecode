package io.srk.pastecode.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    USER_ALREADY_EXISTS("User already exists"),
    SNIPPET_IS_EXPIRED("Current snipped is expired"),
    BLANK_PASSWORD_PROVIDED("Blank password provided"),
    WRONG_PASSWORD_PROVIDED("Passwords don't match");

    private final String message;
}
