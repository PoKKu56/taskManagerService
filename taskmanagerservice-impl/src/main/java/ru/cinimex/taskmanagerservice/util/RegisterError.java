package ru.cinimex.taskmanagerservice.util;

public class RegisterError extends RuntimeException {
    public RegisterError(String message) {
        super(message);
    }
}
