package ru.yandex.practicum.filmorate.exeptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}
