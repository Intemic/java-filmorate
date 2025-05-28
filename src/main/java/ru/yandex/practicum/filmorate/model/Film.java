package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
    private Set<Long> userLiked = new HashSet<>();

    public void checkCorrectData() {
        if (id == null) {
            log.error("ID фильма не указан");
            throw new ValidationException("ID фильма не указан");
        }

        if (name.isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }

        if (description.length() > 200) {
            log.error("Длинна не может быть больше 200 символов");
            throw new ValidationException("Длинна не может быть больше 200 символов");
        }

        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата не может быть ранее 28 декабря 1895 года");
            throw new ValidationException("Дата не может быть ранее 28 декабря 1895 года");
        }

        if (duration <= 0) {
            log.error("Длительность должна быть положительным числом");
            throw new ValidationException("Длительность должна быть положительным числом");
        }
    }
}
