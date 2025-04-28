package ru.cinimex.taskmanagerservice.util;

public class UnknownUserError extends RuntimeException{
    public UnknownUserError(String message) {
        super(message);
    }
}
