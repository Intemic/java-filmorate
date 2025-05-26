package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        user.setId(userStorage.getNextId());
        user.checkCorrectData();
        userStorage.add(user);
        log.info("Создан новый пользователь, id - {}", user.getId());
        return user;
    }

    public User update(User user) {
        if (userStorage.get(user.getId()).isEmpty()) {
            log.error("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }

        user.checkCorrectData();
        User updateUser = userStorage.update(user);
        log.info("Данные пользователя id - {} изменены", user.getId());

        return updateUser;
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = userStorage.get(id);
        if (optionalUser.isEmpty())
            throw new NotFoundResource("Пользователь не найден");

        return optionalUser.get();
    }

    public User addFriend(Long userId, Long friendId) {
        Optional<User> userOptional = userStorage.get(userId);
        if (userOptional.isEmpty())
            throw new NotFoundResource(String.format("Не найден пользователь с id - %d", userId));

        Optional<User> friendOptional = userStorage.get(friendId);
        if (friendOptional.isEmpty())
            throw new NotFoundResource(String.format("Не найден друг с id - %d", friendId));


        return null;
    }
}
