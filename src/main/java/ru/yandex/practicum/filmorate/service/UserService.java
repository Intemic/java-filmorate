package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.frend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utill.Operation;

import static ru.yandex.practicum.filmorate.utill.Operation.ADD;
import static ru.yandex.practicum.filmorate.utill.Operation.DELETE;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("DBUser") UserStorage userStorage,
                       FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Collection<UserDTO> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToFilmDTO)
                .collect(Collectors.toList());
    }

    public User getOneUser(Long id) {
        User user;

        Optional<User> optionalUser = userStorage.get(id);
        if (optionalUser.isEmpty()) {
            log.error(String.format("Не найден пользователь с id - %d", id));
            throw new NotFoundResource(String.format("Не найден пользователь с id - %d", id));
        }

        user = optionalUser.get();

        Optional<Friend> friendOptional = friendStorage.getFriends(id);
        friendOptional.ifPresent(friend -> user.setFriends(friend.getFriends()));
        return user;
    }

    public UserDTO create(NewUserRequest userRequest) {
        userRequest.checkCorrectData();
        if (userRequest.getName() == null || userRequest.getName().isBlank())
            userRequest.setName(userRequest.getLogin());
        User user = userStorage.create(UserMapper.mapToUser(userRequest));
        log.info("Создан новый пользователь, id - {}", user.getId());
        return UserMapper.mapToFilmDTO(user);
    }


    public UserDTO update(UpdateUserRequest userRequest) {
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
                    friendStorage.addFriend(userId, friendId);
                break;
            case DELETE:
                friendStorage.deleteFriend(userId, friendId);
                break;
        }
    }

    public void addFriend(Long userId, Long friendId) {
        changeUserFriend(userId, friendId, ADD);
    }

    public void deleteFriend(Long userId, Long friendId) {
        changeUserFriend(userId, friendId, DELETE);
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
}
