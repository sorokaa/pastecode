package io.srk.pastecode.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ExceptionCode reason;

    public BadRequestException(ExceptionCode reason) {
        this.reason = reason;
    }
}
