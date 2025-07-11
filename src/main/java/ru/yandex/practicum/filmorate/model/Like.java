package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Like {
    private Long id;
    private Long film;
    private Set<Long> users = new LinkedHashSet<>();
}
