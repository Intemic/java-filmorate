package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserExtractor;

import java.util.List;
import java.util.Optional;

@Repository("DBUser")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String BASE_GET_QUERY = "SELECT u.id, " +
            "       u.email, " +
            "       u.login, " +
            "       u.name, " +
            "       u.birthday, " +
            "       uf.friend_id AS friend_id " +
            " FROM users AS u LEFT JOIN users_friends AS uf " +
            "   ON uf.user_id = u.id";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, " +
            "birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String INSERT_FRIEND = "INSERT INTO users_friends (user_id, friend_id) " +
            "VALUES(?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, UserExtractor userExtractor) {
        super(jdbc, mapper);
        super.extractor = userExtractor;
    }

    @Override
    public User create(User user) {
        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<User> getAll() {
        return getManyExtractor(BASE_GET_QUERY);
    }

    @Override
    public Optional<User> get(Long id) {
        String sql = BASE_GET_QUERY + " WHERE u.id = ?";
        return getSingleExtractor(sql, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        insert(INSERT_FRIEND,
                userId,
                friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND,
                userId,
                friendId);
    }

}
