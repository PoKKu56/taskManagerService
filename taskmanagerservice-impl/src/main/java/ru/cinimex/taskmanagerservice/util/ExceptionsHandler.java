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
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(registerError.getMessage());
    }

    @ExceptionHandler(UnknownUserError.class)
    ResponseEntity<?> unknownUserError(final UnknownUserError unknownUserError) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unknownUserError.getMessage());
    }
    @ExceptionHandler(CheckCodeError.class)
    ResponseEntity<?> checkCodeError(final CheckCodeError checkCodeError) {
        return ResponseEntity.status(HttpStatus.valueOf(500)).body(checkCodeError.getMessage());
    }

    @ExceptionHandler(loginError.class)
    ResponseEntity<?> loginError(final loginError loginError) {
        return ResponseEntity.status(HttpStatus.valueOf(403)).body(loginError.getMessage());
    }

}
