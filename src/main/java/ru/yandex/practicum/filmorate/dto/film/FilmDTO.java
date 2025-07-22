package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDTO {
    private Long id;
    private String name;
    private String description;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres;
}
