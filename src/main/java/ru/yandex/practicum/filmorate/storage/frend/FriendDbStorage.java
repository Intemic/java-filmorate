package ru.yandex.practicum.filmorate.storage.frend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Optional;

@Repository
public class FriendDbStorage extends BaseDbStorage<Friend> implements FriendStorage {
    private static final String FIND_ALL_FRIENDS = "SELECT * FROM users_friends WHERE user_id = ?";
    private static final String INSERT_FRIEND = "INSERT INTO users_friends (user_id, friend_id) " +
            "VALUES(?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";

    public FriendDbStorage(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Friend> getFriends(Long userId) {
        return getSingle(FIND_ALL_FRIENDS,
                userId);
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
