package ru.cinimex.taskmanagerservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class ExceptionsHandler {

    @ExceptionHandler(RegisterError.class)
    ResponseEntity<?> registerError(final RegisterError registerError) {
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(registerError.getMessage());
    }

    @ExceptionHandler(UnknowUserError.class)
    ResponseEntity<?> unknowUserError(final UnknowUserError unknowUserError) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unknowUserError.getMessage());
    }

}
