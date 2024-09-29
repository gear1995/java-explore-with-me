package ru.practicum.main.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}