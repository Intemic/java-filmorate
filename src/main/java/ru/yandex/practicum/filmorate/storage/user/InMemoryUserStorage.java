package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        user.setId(getNextId());
        user.checkCorrectData();
        users.put(user.getId(), user);
        log.info("Создан новый пользователь, id - {}", user.getId());

        return user;
    }

    @Override
    public User update(User user) {
        if (get(user.getId()).isEmpty()) {
            log.error("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }

        User oldUser = get(user.getId()).get();
        user.checkCorrectData();

        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());
        if (oldUser.getName().isBlank()) {
            log.warn("Не заполнен логин");
            oldUser.setName(user.getLogin());
        }
        oldUser.setBirthday(user.getBirthday());
        log.info("Данные пользователя id - {} изменены, новые данные: {}", oldUser.getId(), oldUser);

        return oldUser;
    }

    @Override
    public void delete(Long id) {
        if (get(id).isEmpty()) {
            log.error("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }

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

    private Long getNextId() {
        long currentId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentId;
    }
}
