package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Optional;


public interface LikeStorage {
    Optional<Like> getUsersLiked(Long filmId);

    void addUserLiked(Long filmId, Long userId);

    void deleteUserLiked(Long filmId, Long userId);
}
