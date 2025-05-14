package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utill.Mode;

import static ru.yandex.practicum.filmorate.utill.Mode.CREATE;
import static ru.yandex.practicum.filmorate.utill.Mode.UPDATE;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    // придется сделать public чтобы в тестах можно было оперировать данными
    public final Map<Long, User> users = new HashMap<>();

    // тесты были сделаны ранее реализации задания со *, поэтому остались
    public User checkData(User user, Mode mode) {
        User returnUser = user;

        switch (mode) {
            case UPDATE:
                if (user.getId() == null) {
                    log.error("ID пользователя не указан");
                    throw new ValidationException("ID пользователя не указан");
                }

                Optional<User> optionalUser = users.values().stream().filter(usr -> Objects.equals(usr.getId(), user.getId())).findFirst();

                if (optionalUser.isEmpty()) {
                    log.error("Пользователь с id - {} не найден", user.getId());
                    throw new ValidationException("Пользователь не найден");
                }

                returnUser = optionalUser.get();

            case CREATE:
                if (user.getEmail().isBlank() || !user.getEmail().matches("^\\S+@\\w+.\\w+$")) {
                    log.error("Некорректный email - {}", user.getEmail());
                    throw new ValidationException("Некорректный email");
                }

                if (users.values().stream().anyMatch(usr -> usr.getEmail().equals(user.getEmail()))) {
                    log.error("Этот email - {} уже используется", user.getEmail());
                    throw new DuplicatedDataException("Этот email уже используется");
                }

                if (user.getLogin().isBlank()) {
                    log.error("Некорректный login - {}", user.getLogin());
                    throw new ValidationException("Некорректный login");
                }

                if (LocalDate.now().isBefore(user.getBirthday())) {
                    log.error("Некорректная дата рождения");
                    throw new ValidationException("Некорректная дата рождения");
                }

        }

        return returnUser;
    }

    @GetMapping
    public Collection<User> get() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        checkData(user, CREATE);

        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь, id - {}", user.getId());

        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        User oldUser = checkData(user, UPDATE);

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

    public Long getNextId() {
        long currentId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentId;
    }
}
