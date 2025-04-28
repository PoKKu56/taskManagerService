package ru.cinimex.taskmanagerservice.util;

public class CheckCodeError extends RuntimeException {
    public CheckCodeError(String message) {
        super(message);
    }
}
