package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("MemoryFilm")
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film oldFilm = get(film.getId()).get();

        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());

        return oldFilm;
    }

    @Override
    public void delete(Long id) {
        films.remove(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Long getNextId() {
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
