package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NonNull
    @NotBlank
    String name;
    @Length(min = 1, max = 200)
    String description;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    LocalDate releaseDate;
    @NonNull
    Integer duration;
}
