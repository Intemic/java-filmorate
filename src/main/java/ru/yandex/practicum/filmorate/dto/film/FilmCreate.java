package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.dto.director.DirectorShort;
import ru.yandex.practicum.filmorate.dto.genre.GenreShort;
import ru.yandex.practicum.filmorate.dto.mpa.MpaShort;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class FilmCreate {
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200, message = "Длинна не может быть больше 200 символов")
    private String description;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    @Min(value = 1, message = "Длительность должна быть положительным числом")
    private Integer duration;
    private MpaShort mpa;
    private Set<GenreShort> genres = new HashSet<>();
    private Set<DirectorShort> directors = new HashSet<>();

    public void checkCorrectData() {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата не может быть ранее 28 декабря 1895 года");
            throw new ValidationException("Дата не может быть ранее 28 декабря 1895 года");
        }
    }
}