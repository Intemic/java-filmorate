package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmExtractor;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("DBFilm")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String DIRECTOR = "director";
    private static final String TITLE = "title";
    private static final String YEAR = "year";
    private static final String LIKES = "likes";

    private static final String SEARCH_TEMPLATE = "%s ILIKE '%%%s%%'";
    private final FilmExtractor filmExtractor;
    private static final String BASE_GET_QUERY = "SELECT f.id, " +
            "       f.name, " +
            "       f.description, " +
            "       f.release_date, " +
            "       f.duration, " +
            "       f.mpa_id, " +
            "       m.name AS mpa_name, " +
            "       g.id AS genre_id, " +
            "       g.name AS genre_name, " +
            "       fl.user_id AS user_liked, " +
            "       d.id AS director_id, " +
            "       d.name AS director_name " +
            "       FROM FILMS AS f LEFT JOIN MPA AS m " +
            "        ON f.mpa_id = m.id " +
            "       LEFT JOIN FILMS_GENRE AS fg " +
            "         ON fg.film_id = f.id " +
            "       LEFT JOIN GENRES AS g " +
            "         ON g.ID  = fg.GENRE_ID " +
            "       LEFT JOIN films_liked AS fl " +
            "         ON fl.film_id = f.id " +
            "       LEFT JOIN films_director AS fd " +
            "         ON fd.film_id = f.id " +
            "       LEFT JOIN directors AS d " +
            "         ON d.id = fd.director_id ";
    private static final String INSERT_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_LIKE_FOR_FILM = "DELETE FROM films_liked WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_LIKE_FOR_FILM = "INSERT INTO films_liked (film_id, user_id) " +
            "VALUES (?, ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.filmExtractor = filmExtractor;
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
        return jdbc.query(BASE_GET_QUERY, filmExtractor); //getMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> get(Long id) {
        String sql = BASE_GET_QUERY + " WHERE f.id = ?";
        try {
            List<Film> films = jdbc.query(sql, filmExtractor, id);
            if (films.isEmpty())
                return Optional.empty();

            return Optional.of(films.getFirst());
        } catch (DataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        StringBuilder sqlQuery = new StringBuilder();
        Set<String> params = Set.of(by.split(","));

        if (params.contains(DIRECTOR))
            sqlQuery.append(SEARCH_TEMPLATE.formatted("d.name", query));
        if (params.contains(TITLE)) {
            if (!sqlQuery.isEmpty())
                sqlQuery.append(" OR ");
            sqlQuery.append(SEARCH_TEMPLATE.formatted("f.name", query));
        }

        if (sqlQuery.isEmpty())
            return List.of();

        sqlQuery.insert(0, " WHERE ");
        sqlQuery.insert(0, BASE_GET_QUERY);

        return jdbc.query(sqlQuery.toString(), filmExtractor);
    }

    @Override
    public List<Film> getFilmsForDirector(Long directorId, String sortBy) {
        StringBuilder sqlQuery = new StringBuilder(BASE_GET_QUERY)
                .append(" WHERE d.id = " + directorId);
        List<Film> films = jdbc.query(sqlQuery.toString(), filmExtractor);

        if (sortBy.equals(YEAR))
            films = films.stream()
                    .sorted(Comparator.comparingInt(film -> film.getReleaseDate().getYear()))
                    .toList();
        else if (sortBy.equals(LIKES))
          return films = films.stream()
                  .sorted(Comparator.comparingInt(film -> film.getUserLiked().size()))
                  .toList();

        return films;
    }

    @Override
    public List<Film> common(long userId, long friendId) {
        String sqlRequest = BASE_GET_QUERY + " WHERE fl.user_id = " + userId +
                "         AND EXISTS(SELECT * FROM films_liked WHERE film_id = f.id " +
                "                                                AND user_id = " + friendId + ")";
        List<Film> userFilms = jdbc.query(sqlRequest, filmExtractor);
        return userFilms.stream()
                .sorted( (film1, film2) -> film2.getUserLiked().size() - film1.getUserLiked().size())
                .toList();
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
