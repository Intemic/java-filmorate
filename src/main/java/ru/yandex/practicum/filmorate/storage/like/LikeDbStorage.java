package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LikeDbStorage extends BaseDbStorage<Like> implements LikeStorage {
    private static final String FIND_LIKES_FOR_FILM = "SELECT * FROM films_liked WHERE film_id = ?";
    private static final String FIND_LIKES_FOR_ALL_FILMS = "SELECT * FROM films_liked WHERE film_id IN (%s)";
    private static final String DELETE_LIKE_FOR_FILM = "DELETE FROM films_liked WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_LIKE_FOR_FILM = "INSERT INTO films_liked (film_id, user_id) " +
            "VALUES (?, ?)";

    public LikeDbStorage(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Like> getUsersLiked(Long filmId) {
        return getSingle(FIND_LIKES_FOR_FILM, filmId);
    }

    @Override
    public void addUserLiked(Long filmId, Long userId) {
        insert(INSERT_LIKE_FOR_FILM,
                filmId,
                userId);
    }

    @Override
    public void deleteUserLiked(Long filmId, Long userId) {
        delete(DELETE_LIKE_FOR_FILM,
                filmId,
                userId);
    }
}
