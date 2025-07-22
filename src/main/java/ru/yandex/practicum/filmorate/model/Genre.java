package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"})
public class Genre {
    private Long id;
    private String name;
}
