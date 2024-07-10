package ru.cinimex.taskmanagerservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class ExceptionsHandler {

    @ExceptionHandler(RegisterError.class)
    ResponseEntity<String> registerError(final RegisterError registerError) {
        return new ResponseEntity<>(registerError.getMessage(), HttpStatus.CONFLICT);
    }

}
