package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utill.Operation;
import static ru.yandex.practicum.filmorate.utill.Operation.ADD;
import static ru.yandex.practicum.filmorate.utill.Operation.DELETE;

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
        User oldUser = getUser(user.getId());

        user.checkCorrectData();
        User updateUser = userStorage.update(user);
        log.info("Данные пользователя id - {} изменены", user.getId());

        return updateUser;
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = userStorage.get(id);
        if (optionalUser.isEmpty()) {
            log.error(String.format("Не найден пользователь с id - %d", id));
            throw new NotFoundResource(String.format("Не найден пользователь с id - %d", id));
        }

        return optionalUser.get();
    }

    private User changeUserFriend(User user, User userFriend, Operation operation) {
        Set<Long> listFriends = user.getFriends();
        switch (operation) {
            case ADD:
                listFriends.add(userFriend.getId());
                break;
            case DELETE:
                listFriends.remove(userFriend.getId());
                break;
        }
        user.setFriends(listFriends);
        return  userStorage.update(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friendUser = getUser(friendId);

        changeUserFriend(friendUser, user, ADD);
        return changeUserFriend(user, friendUser, ADD);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friendUser = getUser(friendId);

        changeUserFriend(friendUser, user, DELETE);
        changeUserFriend(user, friendUser, DELETE);
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
        User otherUser = getUser(othersId);

        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());

        return intersection.stream()
                . filter(userId -> userStorage.get(userId).isPresent())
                .map( userId ->  (userStorage.get(userId)).get())
                .toList();
    }
}
