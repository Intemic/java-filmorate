package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Friend {
    private Long id;
    private Long user;
    private Set<Long> friends = new LinkedHashSet<>();
}
