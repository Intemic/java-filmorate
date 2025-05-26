package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public User add(User user);

    public User update(User user);

    public void delete(Long id);

    public Collection<User> getAll();

    public Optional<User> get (Long id);
}
