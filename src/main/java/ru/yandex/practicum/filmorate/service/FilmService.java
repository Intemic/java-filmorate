package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmCreate;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdate;
import ru.yandex.practicum.filmorate.dto.genre.GenreShort;
import ru.yandex.practicum.filmorate.dto.mpa.MpaShort;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.utill.Operation;

import static ru.yandex.practicum.filmorate.utill.Operation.ADD;
import static ru.yandex.practicum.filmorate.utill.Operation.DELETE;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("DBFilm") FilmStorage filmStorage,
                       UserService userService,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage,
                       LikeStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeDbStorage = likeDbStorage;
    }

    private List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAll();
//        genreStorage.fillGenresForFilms(films);
//        likeDbStorage.fillLikedForFilms(films);
        return films;
    }

    public List<FilmDTO> getAll() {
        return getAllFilms().stream()
                .map(FilmMapper::mapToFilmDTO)
                .collect(Collectors.toList());
    }

    public FilmDTO create(FilmCreate filmRequest) {
        // проверки
        filmRequest.checkCorrectData();
        checkExistMpa(filmRequest.getMpa());
        checkExistGenres(filmRequest.getGenres());

        Film film = filmStorage.create(FilmMapper.mapToFilm(filmRequest));
        if (!film.getGenres().isEmpty())
            film.setGenres(new LinkedHashSet<>(genreStorage.setGenresForFilm(film)));
        log.info("Создан новый фильм с id - {}", film.getId());
        return FilmMapper.mapToFilmDTO(film);
    }

    public FilmDTO update(FilmUpdate filmRequest) {
        // проверки
        filmRequest.checkCorrectData();
        checkExistMpa(filmRequest.getMpa());
        checkExistGenres(filmRequest.getGenres());

        Film updatedFilm = filmStorage.get(filmRequest.getId())
                .map(film -> FilmMapper.updateFilmFields(film, filmRequest))
                .orElseThrow(() -> new NotFoundResource("Фильм не найден"));

        updatedFilm = filmStorage.update(updatedFilm);
        if (!updatedFilm.getGenres().isEmpty())
            updatedFilm.setGenres(new HashSet<>(genreStorage.updateGenresForFilm(updatedFilm)));

        log.info("Изменены данные фильма id - {}", updatedFilm.getId());
        return FilmMapper.mapToFilmDTO(updatedFilm);
    }

    private Film getOneFilm(Long id) {
        Optional<Film> optionalFilm = filmStorage.get(id);
        if (optionalFilm.isEmpty())
            throw new NotFoundResource("Фильм не найден");

        Film film = optionalFilm.get();
        //genreStorage.fillGenresForFilms(List.of(film));
        //likeDbStorage.fillLikedForFilms(List.of(film));
        return film;
    }

    public FilmDTO getFilm(Long id) {
        return FilmMapper.mapToFilmDTO(getOneFilm(id));
    }

    private void changeLikeTheMove(Long filmId, Long userId, Operation operation) {
        User user = userService.getOneUser(userId);
        Film film = getOneFilm(filmId);

        switch (operation) {
            case ADD:
                Set<Long> userLiked = film.getUserLiked();
                if (!userLiked.contains(user.getId()))
                    likeDbStorage.addUserLiked(film.getId(), user.getId());
                break;
            case DELETE:
                likeDbStorage.deleteUserLiked(film.getId(), user.getId());
                break;
        }
    }

    public void likeTheMove(Long filmId, Long userId) {
        changeLikeTheMove(filmId, userId, ADD);
    }

    public void deleteLikeTheMove(Long filmId, Long userId) {
        changeLikeTheMove(filmId, userId, DELETE);
    }

    public List<FilmDTO> getPopularFilms(int showCount) {
        Comparator<Film> comparatorFilm = (film1, film2) -> {
            return Long.compare(film2.getUserLiked().size(), film1.getUserLiked().size());
        };

        return getAllFilms().stream()
                .sorted(comparatorFilm)
                .skip(Math.max(filmStorage.getAll().size() - (showCount + 1), 0))
                .map(FilmMapper::mapToFilmDTO)
                .toList();
    }

    private void checkExistGenres(Collection<GenreShort> genres) {
        Collection<Genre> genreAll = genreStorage.getAll();
        genres.forEach(genreShort -> {
            if (!genreAll.contains(GenreMapper.mapToGenre(genreShort)))
                throw new NotFoundResource("Жанр - " + genreShort.getId() + " отсутствует");
        });
    }

    private void checkExistMpa(MpaShort mpa) {
        if (mpa != null && mpaStorage.get(mpa.getId()).isEmpty())
            throw new NotFoundResource("MPA - " + mpa.getId() + " отсутствует");
    }

    public List<FilmDTO> search(String query, String by) {
        List<Film> films = filmStorage.searchFilms(query, by);
        genreStorage.fillGenresForFilms(films);
        likeDbStorage.fillLikedForFilms(films);
        return films.stream()
                .sorted((film1, film2) -> (film2.getUserLiked().size() - film1.getUserLiked().size()))
                .map(film -> FilmMapper.mapToFilmDTO(film))
                .toList();
    }
}
