package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface BaseStorage<T> {
    public T create(T object);

    public T update(T object);

    public void delete(Long id);

    public Collection<T> getAll();

    public Optional<T> get(Long id);
}
