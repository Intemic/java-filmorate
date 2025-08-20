package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.genre.GenreShort;
import ru.yandex.practicum.filmorate.model.Genre;

@UtilityClass
public class GenreMapper {
    public static Genre mapToGenre(GenreShort genreShort) {
        Genre genre = new Genre();
        genre.setId(genreShort.getId());
        return genre;
    }

    public static GenreShort mapToShort(Genre genre) {
        GenreShort genreShort = new GenreShort();
        genreShort.setId(genre.getId());
        return genreShort;
    }

}
