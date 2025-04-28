package ru.cinimex.taskmanagerservice.util;

public class loginError extends RuntimeException {
    public loginError(String message) {
        super(message);
    }
}
