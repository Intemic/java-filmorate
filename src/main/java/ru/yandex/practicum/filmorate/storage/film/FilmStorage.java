package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {
    List<Film> searchFilms(String query, String by);

    List<Film> getFilmsForDirector(Long directorId, String sortBy);

    List<Film> common(long userId, long friendId);

    void addUserLiked(Long filmId, Long userId);

    void deleteUserLiked(Long filmId, Long userId);
}
