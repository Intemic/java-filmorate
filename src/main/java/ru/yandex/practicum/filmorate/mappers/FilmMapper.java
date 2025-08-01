package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FilmMapper {
    public static FilmDTO mapToFilmDTO(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        filmDTO.setMpa(film.getMpa());
        filmDTO.setGenres(new LinkedHashSet<>(film.getGenres()));
        return filmDTO;
    }

    public static Film mapToFilm(NewFilmRequest filmRequest) {
        Film film = new Film();
        film.setName(filmRequest.getName());
        film.setDescription(filmRequest.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate());
        film.setDuration(filmRequest.getDuration());
        if (filmRequest.getMpa() != null) {
            Mpa mpa = new Mpa();
            mpa.setId(filmRequest.getMpa().getId());
            film.setMpa(mpa);
        }
        if (!filmRequest.getGenres().isEmpty())
            film.setGenres(new HashSet<Genre>(filmRequest.getGenres().stream()
                    .map(GenreMapper::mapToGenre)
                    .toList()));
        return film;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest updateFilm) {
        if (updateFilm.hasName())
            film.setName(updateFilm.getName());

        if (updateFilm.hasDescription())
            film.setDescription(updateFilm.getDescription());

        if (updateFilm.hasReleaseDate())
            film.setReleaseDate(updateFilm.getReleaseDate());

        if (updateFilm.hasDuration())
            film.setDuration(updateFilm.getDuration());

        if (updateFilm.hasMpa()) {
            Mpa mpa = new Mpa();
            mpa.setId(updateFilm.getMpa().getId());
            film.setMpa(mpa);
        }

        if (updateFilm.hasGenres())
            film.setGenres(new HashSet<Genre>(
                    updateFilm.getGenres().stream()
                            .map(GenreMapper::mapToGenre)
                            .collect(Collectors.toSet())
            ));

        return film;
    }
}
