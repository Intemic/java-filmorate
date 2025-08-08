package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.FilmCreate;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdate;
import ru.yandex.practicum.filmorate.model.Director;
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
        filmDTO.setDirectors(new LinkedHashSet<>(film.getDirectors()));
        return filmDTO;
    }

    public static Film mapToFilm(FilmCreate filmRequest) {
        Film film = new Film();
        film.setName(filmRequest.getName());
        film.setDescription(filmRequest.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate());
        film.setDuration(filmRequest.getDuration());
        if (filmRequest.getMpa() != null) {
//            Mpa mpa = new Mpa();
//            mpa.setId(filmRequest.getMpa().getId());
//            film.setMpa(mpa);
            film.setMpa(Mpa.builder()
                    .id(filmRequest.getMpa().getId())
                    .build());
        }
        if (!filmRequest.getGenres().isEmpty())
            film.setGenres(new HashSet<Genre>(filmRequest.getGenres().stream()
                    .map(GenreMapper::mapToGenre)
                    .toList()));
        if (!filmRequest.getDirectors().isEmpty())
            film.setDirectors(new HashSet<Director>(filmRequest.getDirectors().stream()
                    .map( DirectorMapper::mapShortToDirector)
                    .toList()));

        return film;
    }

    public static Film updateFilmFields(Film film, FilmUpdate updateFilm) {
        if (updateFilm.hasName())
            film.setName(updateFilm.getName());

        if (updateFilm.hasDescription())
            film.setDescription(updateFilm.getDescription());

        if (updateFilm.hasReleaseDate())
            film.setReleaseDate(updateFilm.getReleaseDate());

        if (updateFilm.hasDuration())
            film.setDuration(updateFilm.getDuration());

        if (updateFilm.hasMpa()) {
//            Mpa mpa = new Mpa();
//            mpa.setId(updateFilm.getMpa().getId());
//            film.setMpa(mpa);
            film.setMpa(Mpa.builder()
                    .id(updateFilm.getMpa().getId())
                    .build());
        }

        if (updateFilm.hasGenres())
            film.setGenres(new HashSet<Genre>(
                    updateFilm.getGenres().stream()
                            .map(GenreMapper::mapToGenre)
                            .collect(Collectors.toSet())
            ));

        if (updateFilm.hasDirectors())
            film.setDirectors(updateFilm.getDirectors().stream()
                    .map(directorShort -> Director.builder().id(directorShort.getId()).build())
                    .collect(Collectors.toSet()));

        return film;
    }
}
