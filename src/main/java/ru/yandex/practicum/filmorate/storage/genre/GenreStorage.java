package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> getAll();

    Optional<Genre> get(Long id);

    Collection<Genre> getGenresForFilm(Film film);

    Collection<Genre> setGenresForFilm(Film film);

    Collection<Genre> updateGenresForFilm(Film film);
}
