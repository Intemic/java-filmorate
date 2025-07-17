package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.config.AppConfig;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final AppConfig appConfig;

    @GetMapping
    public List<FilmDTO> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public FilmDTO getFilm(@PathVariable @Positive Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<FilmDTO> getPopularFilms(
            @RequestParam(required = false) @Positive Integer count) {
        if (count == null)
            count = appConfig.getFilm().getPopular().getShow().getCount();
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDTO create(@RequestBody @Valid NewFilmRequest film) {
        return filmService.create(film);
    }

    @PutMapping
    public FilmDTO update(@RequestBody @Valid UpdateFilmRequest filmRequest) {
        return filmService.update(filmRequest);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void likeTheMove(@PathVariable("id") @Positive Long filmId,
                            @PathVariable("userId") @Positive Long userId) {
        filmService.likeTheMove(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeTheMove(@PathVariable("id") @Positive Long filmId,
                                  @PathVariable("userId") @Positive Long userId) {
        filmService.deleteLikeTheMove(filmId, userId);
    }

}
