package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        long mid = rs.getLong("mpa_id");
        if (mid != 0) {
//            Mpa mpa = new Mpa();
//            mpa.setId(mid);
//            mpa.setName(rs.getString("mpa_name"));
//            film.setMpa(mpa);
            film.setMpa(Mpa.builder()
                    .id(mid)
                    .name(rs.getString("mpa_name"))
                    .build());
        }
        return film;
    }
}
