package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_FILM_GENRES = "SELECT g.id, g.name FROM FILMS_GENRE AS fg " +
            "INNER JOIN GENRES g ON g.ID  = fg.GENRE_ID WHERE fg.film_id = ? ORDER BY g.id";
    private static final String FIND_ALL_GENRES_FOR_FILMS = "SELECT fg.film_id, g.id, g.name FROM FILMS_GENRE AS fg " +
            "INNER JOIN GENRES g ON g.ID  = fg.GENRE_ID WHERE fg.film_id IN (%s)";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO films_genre (film_id, genre_id)" +
            "VALUES(?, ?)";
    private static final String DELETE_GENRES_FOR_FILM = "DELETE FROM films_genre WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAll() {
        return getMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> get(Long id) {
        return getSingle(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Genre> getGenresForFilm(Film film) {
        return getMany(FIND_ALL_FILM_GENRES, film.getId());
    }

    @Override
    public Collection<Genre> setGenresForFilm(Film film) {
        Set<Genre> genres = film.getGenres();
        Iterator<Genre> iterator = genres.iterator();

        jdbc.batchUpdate(INSERT_GENRE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = iterator.next();
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

        return getGenresForFilm(film);
    }

    @Override
    public Collection<Genre> updateGenresForFilm(Film film) {
        delete(DELETE_GENRES_FOR_FILM, film.getId());
        setGenresForFilm(film);
        return getGenresForFilm(film);
    }

}
