package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getLong("id"))
                .film(rs.getLong("film_id"))
                .user(rs.getLong("user_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("positiv"))
                .useful(rs.getInt("useful"))
                .build();
    }
}
