package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            Film film = filmMap.computeIfAbsent(id,
                    aLong -> {
                        try {
                            return Film.builder()
                                    .id(id)
                                    .name(rs.getString("name"))
                                    .description(rs.getString("description"))
                                    .releaseDate(rs.getDate("release_date").toLocalDate())
                                    .duration(rs.getInt("duration"))
                                    .mpa(rs.getLong("mpa_id") != 0 ?
                                            Mpa.builder()
                                                    .id(rs.getLong("mpa_id"))
                                                    .name(rs.getString("mpa_name"))
                                                    .build() : null)
                                    .genres(new TreeSet<>(Comparator.comparing(Genre::getId)))
                                    .userLiked(new TreeSet<>(Long::compare))
                                    .directors(new TreeSet<>(Comparator.comparing(Director::getId)))
                                    .build();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            if (rs.getLong("genre_id") != 0)
                film.getGenres().add(Genre.builder()
                        .id(rs.getLong("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build());

            if (rs.getLong("user_liked") != 0)
                film.getUserLiked().add(rs.getLong("user_liked"));

            if (rs.getLong("director_id") != 0)
                film.getDirectors().add(Director.builder()
                        .id(rs.getLong("director_id"))
                        .name(rs.getString("director_name"))
                        .build());
        }

        return filmMap.values().stream().toList();
    }
}
