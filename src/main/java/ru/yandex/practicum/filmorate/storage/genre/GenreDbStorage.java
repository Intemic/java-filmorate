package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbChangedStore;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage extends BaseDbChangedStore<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_FILM_GENRES = "SELECT g.id, g.name FROM FILMS_GENRE AS fg " +
            "INNER JOIN GENRES g ON g.ID  = fg.GENRE_ID WHERE fg.film_id = ? ORDER BY g.id";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO films_genre (film_id, genre_id)" +
            "VALUES(?, ?)";

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
        film.getGenres()
                .forEach(genre ->
                        insert(INSERT_GENRE_QUERY, film.getId(), genre.getId()));
        return getGenresForFilm(film);
    }

    @Override
    public Collection<Genre> updateGenresForFilm(Film film) {
        Collection<Genre> genresExist = getGenresForFilm(film);
        Set<Genre> newGenres = film.getGenres().stream()
                .filter(genre -> !genresExist.contains(genre))
                .collect(Collectors.toSet());
        if (!newGenres.isEmpty()) {
            Film tempFilm = new Film();
            tempFilm.setId(film.getId());
            tempFilm.setGenres(newGenres);
            setGenresForFilm(tempFilm);
        }

        return getGenresForFilm(film);
    }

}
