package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.exeptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;

import java.time.LocalDate;

@Slf4j
@Data
public class User {
    private Long id;
    @NonNull
    @Email
    @NotBlank
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate birthday;

    public void checkCorrectData() {
        if (id == null) {
            log.error("ID пользователя не указан");
            throw new ValidationException("ID пользователя не указан");
        }

        if (email.isBlank() || !email.matches("^\\S+@\\w+.\\w+$")) {
            log.error("Некорректный email - {}", email);
            throw new ValidationException("Некорректный email");
        }

        if (login.isBlank()) {
            log.error("Некорректный login - {}", login);
            throw new ValidationException("Некорректный login");
        }

        if (LocalDate.now().isBefore(birthday)) {
            log.error("Некорректная дата рождения");
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
