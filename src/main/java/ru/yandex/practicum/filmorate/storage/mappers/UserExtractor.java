package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> users = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            User user = users.computeIfAbsent(rs.getLong("id"),
                    aLong -> {
                        try {
                            return User.builder()
                                    .id(id)
                                    .email(rs.getString("email"))
                                    .login(rs.getString("login"))
                                    .name(rs.getString("name"))
                                    .birthday(rs.getDate("birthday").toLocalDate())
                                    .friends(new TreeSet<>(Long::compare))
                                    .build();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            if (rs.getLong("friend_id") != 0)
                user.getFriends().add(rs.getLong("friend_id"));
        }

        return users.values().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
    }
}
