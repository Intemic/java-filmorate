package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.utill.Operation;

import static ru.yandex.practicum.filmorate.utill.Operation.ADD;
import static ru.yandex.practicum.filmorate.utill.Operation.DELETE;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

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

    private void changeLikeTheMove(Film film, User user, Operation operation) {
        Set<Long> userLiked = film.getUserLiked();
        switch (operation) {
            case ADD:
                userLiked.add(user.getId());
                break;
            case DELETE:
                userLiked.remove(user.getId());
                break;
        }
        film.setUserLiked(userLiked);
        filmStorage.update(film);
    }

    public void likeTheMove(Long filmId, Long userId) {
        User user = userService.getUser(userId);
        Film film = getFilm(filmId);

        changeLikeTheMove(film, user, ADD);
    }

    public void deleteLikeTheMove(Long filmId, Long userId) {
        User user = userService.getUser(userId);
        Film film = getFilm(filmId);

        changeLikeTheMove(film, user, DELETE);
    }

    public List<Film> getPopularFilms(int showCount) {
        Comparator<Film> comparatorFilm = (film1, film2) -> {
           return Long.compare(film2.getUserLiked().size(), film1.getUserLiked().size());
        };

        return filmStorage.getAll().stream()
                .sorted(comparatorFilm)
                .skip(Math.max(filmStorage.getAll().size() - (showCount + 1), 0))
                .toList();
    }
}
