package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.dto.director.DirectorShort;
import ru.yandex.practicum.filmorate.dto.genre.GenreShort;
import ru.yandex.practicum.filmorate.dto.mpa.MpaShort;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class FilmUpdate{
    @NotNull
    private Long id;
    private String name;
    @Size(max = 200, message = "Длинна не может быть больше 200 символов")
    private String description;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private Integer duration;
    private MpaShort mpa;
    private Set<GenreShort> genres = new HashSet<>(); //LinkedHashSet<>();
    private Set<DirectorShort> directors = new HashSet<>();

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasDuration() {
        return !(duration == null);
    }

    public boolean hasMpa() {
        return !(mpa == null);
    }

    public boolean hasGenres() {
        return genres != null; //!(genres == null || genres.isEmpty());
    }

    public boolean hasDirectors() {
        return directors != null;
    }

    public void checkCorrectData() {
        if (hasReleaseDate() && releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата не может быть ранее 28 декабря 1895 года");
            throw new ValidationException("Дата не может быть ранее 28 декабря 1895 года");
        }

        if (hasDuration() && duration <= 0) {
            log.error("Длительность должна быть положительным числом");
            throw new ValidationException("Длительность должна быть положительным числом");
        }

    }
}
