package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotFoundResource.class, ValidationException.class})
    public ErrorResponse handleNotFoundResource(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
