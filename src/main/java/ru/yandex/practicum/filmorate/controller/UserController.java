package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> get() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable @Positive Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable @Positive Long id,
                                       @PathVariable("friendId") @Positive Long othersId) {
        return userService.getMutualFriends(id, othersId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") @Positive Long userId,
                          @PathVariable("friendId") @Positive Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable("id") @Positive Long userId,
                             @PathVariable("friendId") @Positive Long friendId) {
        userService.deleteFriend(userId, friendId);
    }
}
