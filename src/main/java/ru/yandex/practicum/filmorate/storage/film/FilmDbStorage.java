package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository("DBFilm")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, m.name AS mpa_name FROM FILMS AS f LEFT JOIN MPA AS m " +
            "ON f.mpa_id = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa_id, m.name AS mpa_name FROM FILMS AS f LEFT JOIN MPA AS m " +
            "ON f.mpa_id = m.id WHERE f.id = ?";

    private static final String INSERT_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId()

        );
        return film;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<Film> getAll() {
        return getMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> get(Long id) {
        return getSingle(FIND_BY_ID_QUERY, id);
    }

}
