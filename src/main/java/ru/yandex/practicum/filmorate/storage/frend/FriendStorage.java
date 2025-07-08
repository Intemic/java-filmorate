package ru.yandex.practicum.filmorate.storage.frend;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Optional;

public interface FriendStorage {
    Optional<Friend> getFriends(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}
