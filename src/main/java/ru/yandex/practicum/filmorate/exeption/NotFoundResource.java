package ru.yandex.practicum.filmorate.exeption;

public class NotFoundResource extends RuntimeException{
    public NotFoundResource(String msg) {
        super(msg);
    }
}
