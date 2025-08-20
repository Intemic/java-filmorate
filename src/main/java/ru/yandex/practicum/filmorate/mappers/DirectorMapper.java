package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.director.DirectorCreate;
import ru.yandex.practicum.filmorate.dto.director.DirectorDTO;
import ru.yandex.practicum.filmorate.dto.director.DirectorShort;
import ru.yandex.practicum.filmorate.dto.director.DirectorUpdate;
import ru.yandex.practicum.filmorate.model.Director;

@UtilityClass
public class DirectorMapper {
    public static DirectorDTO mapDirectorDTO(Director director) {
        return DirectorDTO.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
    }

    public static Director mapShortToDirector(DirectorShort directorCreate) {
        return Director.builder()
                .id(directorCreate.getId())
                .build();
    }

    public static Director mapToDirector(DirectorCreate directorCreate) {
        return Director.builder()
                .name(directorCreate.getName())
                .build();
    }

    public static Director updateDirectorFields(Director director, DirectorUpdate directorUpdate) {
        if (directorUpdate.hasName())
            director.setName(directorUpdate.getName());

        return director;
    }

}
