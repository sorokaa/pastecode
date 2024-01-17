package io.srk.pastecode.common.controller;

import io.srk.pastecode.common.exception.BadRequestException;
import io.srk.pastecode.common.exception.ExceptionCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestException(BadRequestException exception) {
        ExceptionCode reason = exception.getReason();
        Map<String, Object> responseBody = Map.of(
                "message", reason.getMessage(),
                "timestamp", Instant.now()
        );
        return ResponseEntity
                .badRequest()
                .body(responseBody);
    }
}
