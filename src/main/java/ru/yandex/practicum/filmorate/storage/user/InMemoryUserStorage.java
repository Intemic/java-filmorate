package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component("MemoryUser")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User update(User user) {
        User oldUser = get(user.getId()).get();

        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());
        if (oldUser.getName().isBlank()) {
            oldUser.setName(user.getLogin());
        }
        oldUser.setBirthday(user.getBirthday());

        return oldUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> get(Long id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Long getNextId() {
        long currentId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentId;
    }
}
