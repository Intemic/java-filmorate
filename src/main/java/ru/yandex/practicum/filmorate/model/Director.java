package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private Long id;
    private String name;
}
