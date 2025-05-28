package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    final String error;
}
