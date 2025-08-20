package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.utill.EventType;
import ru.yandex.practicum.filmorate.utill.Operation;

import java.sql.Timestamp;

@Builder
@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Feed {
    private Long id;
    private Timestamp timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long entityId;
}
