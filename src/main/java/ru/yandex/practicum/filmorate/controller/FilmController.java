package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utill.Mode;

import static ru.yandex.practicum.filmorate.utill.Mode.CREATE;
import static ru.yandex.practicum.filmorate.utill.Mode.UPDATE;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    // придется сделать public чтобы в тестах можно было оперировать данными
    public final Map<Long, Film> films = new HashMap<>();

    // тесты были сделаны ранее реализации задания со *, поэтому остались
    public Film checkData(Film film, Mode mode) {
        Film returnFilm = film;

        switch (mode) {
            case UPDATE:
                if (film.getId() == null) {
                    log.error("ID фильма не указан");
                    throw new ValidationException("ID фильма не указан");
                }

                returnFilm = films.get(film.getId());

                if (returnFilm == null) {
                    log.error("Фильм не найден");
                    throw new ValidationException("Фильм не найден");
                }

            case CREATE:
                if (film.getName().isBlank()) {
                    log.error("Название не может быть пустым");
                    throw new ValidationException("Название не может быть пустым");
                }

                if (film.getDescription().length() > 200) {
                    log.error("Длинна не может быть больше 200 символов");
                    throw new ValidationException("Длинна не может быть больше 200 символов");
                }

                if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                    log.error("Дата не может быть ранее 28 декабря 1895 года");
                    throw new ValidationException("Дата не может быть ранее 28 декабря 1895 года");
                }

                if (film.getDuration() <= 0) {
                    log.error("Длительность должна быть положительным числом");
                    throw new ValidationException("Длительность должна быть положительным числом");
                }
        }

        return returnFilm;
    }

    @GetMapping
    public Collection<Film> get() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @NonNull @Valid Film film) {
        checkData(film, CREATE);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан новый фильм с id - {}", film.getId());

        return film;
    }

    @PutMapping
    public Film update(@RequestBody @NonNull @Valid Film film) {
        Film oldFilm = checkData(film, UPDATE);

        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        log.info("Изменены данные фильма id - {}, новые данные {}", film.getId(), film);

        return oldFilm;
    }

    public Long getNextId() {
        long currentId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }

}
