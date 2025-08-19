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
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
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
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmService(@Qualifier("DBFilm") FilmStorage filmStorage,
                       UserService userService,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage,
                       DirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.directorStorage = directorStorage;
    }

    public List<FilmDTO> getAll() {
        return filmStorage.getAll().stream()
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
        if (!film.getDirectors().isEmpty())
            film.setDirectors(new LinkedHashSet<>(directorStorage.setDirectorsForFilm(film)));

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
        updatedFilm.setGenres(new HashSet<>(genreStorage.updateGenresForFilm(updatedFilm)));
        updatedFilm.setDirectors(new HashSet<>(directorStorage.updateDirectorsForFilm(updatedFilm)));

        log.info("Изменены данные фильма id - {}", updatedFilm.getId());
        return FilmMapper.mapToFilmDTO(updatedFilm);
    }

    private Film getOneFilm(Long id) {
        Optional<Film> optionalFilm = filmStorage.get(id);
        if (optionalFilm.isEmpty())
            throw new NotFoundResource("Фильм не найден");

        Film film = optionalFilm.get();
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
                    filmStorage.addUserLiked(film.getId(), user.getId());
                break;
            case DELETE:
                filmStorage.deleteUserLiked(film.getId(), user.getId());
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
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> (film2.getUserLiked().size() - film1.getUserLiked().size()))
                .limit(showCount)
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
        return filmStorage.searchFilms(query, by).stream()
                .sorted((film1, film2) -> (film2.getUserLiked().size() - film1.getUserLiked().size()))
                .map(film -> FilmMapper.mapToFilmDTO(film))
                .toList();
    }

    public List<FilmDTO> getFilmsForDirector(Long directorId, String sortBy) {
        if (directorStorage.get(directorId).isEmpty())
            throw new NotFoundResource("Директор не найден");
        return filmStorage.getFilmsForDirector(directorId, sortBy).stream()
                .map(FilmMapper::mapToFilmDTO).toList();
    }

    public void deleteFilm(Long filmId) {
        getOneFilm(filmId);
        filmStorage.delete(filmId);
    }

    public List<FilmDTO> common(long userId, long friendId) {
       // проверки
       userService.getOneUser(userId);
       userService.getOneUser(friendId);

       return filmStorage.common(userId, friendId).stream()
               .map( film -> FilmMapper.mapToFilmDTO(film))
               .toList();
    }
}
