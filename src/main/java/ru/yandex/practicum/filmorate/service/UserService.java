package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.feed.FeedDTO;
import ru.yandex.practicum.filmorate.dto.user.UserCreate;
import ru.yandex.practicum.filmorate.dto.user.UserUpdate;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.mappers.FeedMapper;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utill.Operation;

import static ru.yandex.practicum.filmorate.utill.Operation.ADD;
import static ru.yandex.practicum.filmorate.utill.Operation.REMOVE;
import static ru.yandex.practicum.filmorate.utill.EventType.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    public UserService(@Qualifier("DBUser") UserStorage userStorage,
                       FeedStorage feedStorage) {
        this.userStorage = userStorage;
        this.feedStorage = feedStorage;
    }

    public List<UserDTO> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToFilmDTO)
                .collect(Collectors.toList());
    }

    public User getOneUser(Long id) {
        Optional<User> optionalUser = userStorage.get(id);
        if (optionalUser.isEmpty()) {
            log.error(String.format("Не найден пользователь с id - %d", id));
            throw new NotFoundResource(String.format("Не найден пользователь с id - %d", id));
        }

        return optionalUser.get();
    }

    public UserDTO create(UserCreate userRequest) {
        userRequest.checkCorrectData();
        if (userRequest.getName() == null || userRequest.getName().isBlank())
            userRequest.setName(userRequest.getLogin());
        User user = userStorage.create(UserMapper.mapToUser(userRequest));
        log.info("Создан новый пользователь, id - {}", user.getId());
        return UserMapper.mapToFilmDTO(user);
    }


    public UserDTO update(UserUpdate userRequest) {
        userRequest.checkCorrectData();
        User oldUser = getOneUser(userRequest.getId());
        User updateUser = userStorage.update(UserMapper.updateUserFields(oldUser, userRequest));
        log.info("Данные пользователя id - {} изменены", updateUser.getId());

        return UserMapper.mapToFilmDTO(updateUser);
    }

    public UserDTO getUser(Long id) {
        return UserMapper.mapToFilmDTO(getOneUser(id));
    }

    private void changeUserFriend(Long userId, Long friendId, Operation operation) {
        User user = getOneUser(userId);
        User friendUser = getOneUser(friendId);

        switch (operation) {
            case ADD:
                Set<Long> listFriends = user.getFriends();
                if (!listFriends.contains(friendId))
                    userStorage.addFriend(userId, friendId);
                break;
            case REMOVE:
                userStorage.deleteFriend(userId, friendId);
                break;
            default:
                return;
        }

        feedStorage.insert(userId,
                FRIEND,
                operation,
                friendId);
    }

    public void addFriend(Long userId, Long friendId) {
        changeUserFriend(userId, friendId, ADD);
    }

    public void deleteFriend(Long userId, Long friendId) {
        changeUserFriend(userId, friendId, REMOVE);
    }

    public List<User> getFriends(Long id) {
        User user = getOneUser(id);
        return user.getFriends().stream()
                .filter(userId -> userStorage.get(userId).isPresent())
                .map(userId -> (userStorage.get(userId)).get())
                .toList();
    }

    public List<User> getMutualFriends(Long id, Long othersId) {
        User user = getOneUser(id);
        User otherUser = getOneUser(othersId);

        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());

        return intersection.stream()
                .filter(userId -> userStorage.get(userId).isPresent())
                .map(userId -> (userStorage.get(userId)).get())
                .toList();
    }

    public void deleteUser(Long userId) {
        getOneUser(userId);
        userStorage.delete(userId);
    }

    public List<FeedDTO> getFeeds(Long id) {
        getOneUser(id);
        return feedStorage.getFeeds(id).stream()
                .map(feed -> FeedMapper.mapToFeedDTO(feed))
                .toList();
    }
}
