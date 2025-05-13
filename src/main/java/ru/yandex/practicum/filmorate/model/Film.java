package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NonNull
    String name;
    String description;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    LocalDate releaseDate;
    @NonNull
    Integer duration;
}
