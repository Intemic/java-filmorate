package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public void add(Film film);

    public Film update(Film film);

    public void delete(Long id);

    public Collection<Film> getAll();

    public Optional<Film> get (Long id);

    public Long getNextId();
}
