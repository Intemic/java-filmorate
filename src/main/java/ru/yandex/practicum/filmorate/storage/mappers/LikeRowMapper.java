package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        LinkedHashSet<Long> users = new LinkedHashSet();
        Like like = new Like();
        like.setId(rs.getLong("id"));
        like.setFilm(rs.getLong("film_id"));
        users.add(rs.getLong("user_id"));

        while (rs.next())
            users.add(rs.getLong("user_id"));

        if (!users.isEmpty())
            like.setUsers(users);

        return like;
    }
}
