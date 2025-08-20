package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.utill.EventType;
import ru.yandex.practicum.filmorate.utill.Operation;

import java.util.List;

public interface FeedStorage {
    void insert(Long userId,
                EventType eventType,
                Operation operation,
                Long entityId);

    List<Feed> getFeeds(Long userId);
}
