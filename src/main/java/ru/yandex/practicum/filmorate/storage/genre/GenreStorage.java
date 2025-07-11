package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    public Collection<Genre> getAll();

    public Optional<Genre> get(Long id);

    public Collection<Genre> getGenresForFilm(Film film);

    public Collection<Genre> setGenresForFilm(Film film);

    public Collection<Genre> updateGenresForFilm(Film film);
}
