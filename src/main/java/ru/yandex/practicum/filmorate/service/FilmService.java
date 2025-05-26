package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        film.setId(filmStorage.getNextId());
        film.checkCorrectData();
        filmStorage.add(film);
        log.info("Создан новый фильм с id - {}", film.getId());
        return film;
    }

    public Film update(Film film) {
        if (filmStorage.get(film.getId()).isEmpty()) {
            log.error("Фильм не найден");
            throw new NotFoundResource("Фильм не найден");
        }

        film.checkCorrectData();
        Film updatedFilm = filmStorage.update(film);
        log.info("Изменены данные фильма id - {}", film.getId());

        return updatedFilm;
    }

    public Film getFilm(Long id) {
       Optional<Film> optionalFilm = filmStorage.get(id);
       if (optionalFilm.isEmpty())
           throw new NotFoundResource("Фильм не найден");

       return optionalFilm.get();
    }
}
