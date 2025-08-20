package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.feed.FeedDTO;
import ru.yandex.practicum.filmorate.model.Feed;

@UtilityClass
public class FeedMapper {
    public static FeedDTO mapToFeedDTO(Feed feed) {
        return FeedDTO.builder()
                .id(feed.getId())
                .userId(feed.getUserId())
                .timestamp(feed.getTimestamp().getTime())
                .eventType(feed.getEventType().name())
                .operation(feed.getOperation().name())
                .entityId(feed.getEntityId())
                .build();
    }
}
