package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Film {
    private Long id;
    private String name;
    private String description;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
    private Long rating;
    private Mpa mpa;
    private Set<Long> userLiked = new LinkedHashSet<>();
    private Set<Genre> genres = new LinkedHashSet<>();

    public Film() {
    }

    public Film(String name, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
