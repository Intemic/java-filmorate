package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private Long id;
    private Long user;
    private Long film;
    private String content;
    private boolean isPositive;
    private int useful;
}
