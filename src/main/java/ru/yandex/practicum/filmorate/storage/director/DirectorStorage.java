package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;

public interface DirectorStorage extends BaseStorage<Director> {

   Collection<Director> setDirectorsForFilm(Film film);

   Collection<Director> getDirectorsForFilm(Film film);

   Collection<Director> updateDirectorsForFilm(Film film);
}
