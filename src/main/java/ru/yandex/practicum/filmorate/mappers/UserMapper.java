package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.user.UserCreate;
import ru.yandex.practicum.filmorate.dto.user.UserUpdate;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static UserDTO mapToFilmDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setLogin(user.getLogin());
        userDTO.setBirthday(user.getBirthday());
        return userDTO;
    }

    public static User mapToUser(UserCreate userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setLogin(userRequest.getLogin());
        user.setBirthday(userRequest.getBirthday());
        return user;
    }

    public static User updateUserFields(User user, UserUpdate userRequest) {
        if (userRequest.hasEmail())
            user.setEmail(userRequest.getEmail());

        if (userRequest.hasName())
            user.setName(userRequest.getName());

        if (userRequest.hasLogin())
            user.setLogin(userRequest.getLogin());

        if (userRequest.hasBirthday())
            user.setBirthday(userRequest.getBirthday());

        return user;
    }
}
