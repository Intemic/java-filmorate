package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@Component
public class FriendRowMapper implements RowMapper<Friend> {
    @Override
    public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
        LinkedHashSet<Long> friends = new LinkedHashSet();

        Friend friend = new Friend();
        friend.setId(rs.getLong("id"));
        friend.setUser(rs.getLong("user_id"));
        friends.add(rs.getLong("friend_id"));

        while (rs.next())
            friends.add(rs.getLong("user_id"));

        if (!friends.isEmpty())
            friend.setFriends(friends);

        return friend;
    }
}
