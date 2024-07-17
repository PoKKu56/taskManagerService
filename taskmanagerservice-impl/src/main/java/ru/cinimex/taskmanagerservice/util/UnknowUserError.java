package ru.cinimex.taskmanagerservice.util;

public class UnknowUserError extends RuntimeException{
    public UnknowUserError(String message) {
        super(message);
    }
}
