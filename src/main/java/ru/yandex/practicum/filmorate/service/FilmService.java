package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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

    public void likeTheMove(Long filmId, Long userId) {
        User user = userService.getUser(userId);
        Film film = getFilm(filmId);

        Set<Long> userLiked = film.getUserLiked();
        userLiked.add(user.getId());
        film.setUserLiked(userLiked);

        filmStorage.update(film);
    }

    public void deleteLikeTheMove(Long filmId, Long userId) {
        User user = userService.getUser(userId);
        Film film = getFilm(filmId);

        Set<Long> userLiked = film.getUserLiked();
        userLiked.remove(user.getId());
        film.setUserLiked(userLiked);

        filmStorage.update(film);
    }

    public List<Film> getPopularFilms(int showCount) {
        Comparator<Film> comparatorFilm = (film1, film2 ) -> {
            if (film2.getUserLiked().size() > film1.getUserLiked().size())
                return 1;
            else if( film2.getUserLiked().size() < film1.getUserLiked().size())
                return -1;

            return 0;
        };

        int skip = filmStorage.getAll().size() - (showCount + 1);
        return filmStorage.getAll().stream()
                .sorted(comparatorFilm)
                .skip ( skip > 0 ? skip : 0 )
                .toList();
    }
}
