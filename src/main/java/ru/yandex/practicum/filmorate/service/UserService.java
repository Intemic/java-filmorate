package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

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
            throw new NotFoundResource(String.format("Не найден пользователь с id - %d", id));

        return optionalUser.get();
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(userId);

        Set<Long> friends = user.getFriends();
        friends.add(friendId);
        user.setFriends(friends);
        return userStorage.update(user);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(userId);

        Set<Long> friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);
        userStorage.update(user);
    }

    public List<User> getFriends(Long id) {
        User user = getUser(id);
        return user.getFriends().stream()
                . filter(userId -> userStorage.get(userId).isPresent())
                .map( userId ->  (userStorage.get(userId)).get())
                .toList();
    }

    public List<User> getMutualFriends(Long id, Long othersId) {
        User user = getUser(id);
        User otherUser = getUser(id);

        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());

        return intersection.stream()
                . filter(userId -> userStorage.get(userId).isPresent())
                .map( userId ->  (userStorage.get(userId)).get())
                .toList();
    }
}
