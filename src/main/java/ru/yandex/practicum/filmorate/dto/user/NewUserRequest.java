package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.time.LocalDate;

@Data
@Slf4j
public class NewUserRequest {
    @NonNull
    @NotBlank(message = "Некорректный email")
    @Email(message = "Некорректный email")
    private String email;
    @NonNull
    @NotBlank(message = "Некорректный login")
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate birthday;

    public void checkCorrectData() {
        if (LocalDate.now().isBefore(birthday)) {
            log.error("Некорректная дата рождения");
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
