package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(getNextId());
        film.checkCorrectData();
        films.put(film.getId(), film);
        log.info("Создан новый фильм с id - {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (get(film.getId()).isEmpty()) {
            log.error("Фильм не найден");
            throw new ValidationException("Фильм не найден");
        }

        Film oldFilm = get(film.getId()).get();
        film.checkCorrectData();

        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        log.info("Изменены данные фильма id - {}, новые данные {}", film.getId(), film);

        return oldFilm;
    }

    @Override
    public void delete(Long id) {
        if (get(id).isEmpty()) {
            log.error("Фильм не найден");
            throw new ValidationException("Фильм не найден");
        }

        films.remove(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    private Long getNextId() {
        long currentId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }

    @Override
    public Optional<Film> get(Long id) {
        return films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();
    }
}
