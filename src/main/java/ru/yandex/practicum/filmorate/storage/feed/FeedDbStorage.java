package ru.yandex.practicum.filmorate.storage.feed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utill.EventType;
import ru.yandex.practicum.filmorate.utill.Operation;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public class FeedDbStorage extends BaseDbStorage<Feed> implements FeedStorage {
    private static String GET_QUERY = "SELECT * FROM feeds WHERE user_id = ?";
    private static String INSERT_QUERY = "INSERT INTO feeds " +
            "(time_stamp, user_id, event_type, operation, entity_id) " +
            "VALUES(?, ?, ?, ?, ?)";

    public FeedDbStorage(JdbcTemplate jdbc, RowMapper<Feed> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void insert(Long userId,
                       EventType eventType,
                       Operation operation,
                       Long entityId) {
        insert(INSERT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                eventType.name(),
                operation.name(),
                entityId);
    }

    @Override
    public List<Feed> getFeeds(Long userId) {
        return getMany(GET_QUERY, userId);
    }
}
