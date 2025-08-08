package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {
    private static final String BASE_GET_QUERY = "SELECT id, name FROM directors";
    private static final String INSERT_QUERY = "INSERT INTO directors (name) VALUES(?)";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String INSERT_DIRECTORS_QUERY = "INSERT INTO films_director " +
            "(film_id, director_id) VALUES (?, ?)";
    private static final String GET_DIRECTORS_FOR_FILM = "SELECT d.id, d.name " +
            " FROM films_director AS fd INNER JOIN directors AS d " +
            "   ON d.id = fd.director_id " +
            " WHERE film_id = ?";
    private static final String DELETE_DIRECTORS_FOR_FILM = "DELETE FROM films_director WHERE film_id = ?";

    public DirectorDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Director create(Director director) {
        long id = insert(INSERT_QUERY, director.getName());
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director object) {
        update(UPDATE_QUERY,
                object.getName(),
                object.getId());
        return object;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<Director> getAll() {
        return getMany(BASE_GET_QUERY);
    }

    @Override
    public Optional<Director> get(Long id) {
        String query = BASE_GET_QUERY + " WHERE id = ?";
        return getSingle(query, id);
    }

    @Override
    public Collection<Director> setDirectorsForFilm(Film film) {
        Set<Director> directors = film.getDirectors();
        Iterator<Director> iterator = directors.iterator();

        jdbc.batchUpdate(INSERT_DIRECTORS_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Director director = iterator.next();
                ps.setLong(1, film.getId());
                ps.setLong(2, director.getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        });

      return getDirectorsForFilm(film);
    }

    @Override
    public Collection<Director> getDirectorsForFilm(Film film) {
        return getMany(GET_DIRECTORS_FOR_FILM, film.getId());
    }

    @Override
    public Collection<Director> updateDirectorsForFilm(Film film) {
        delete(DELETE_DIRECTORS_FOR_FILM, film.getId());
        setDirectorsForFilm(film);

        return getDirectorsForFilm(film);
    }
}
