package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.time.LocalDate;

@Slf4j
@Data
public class UpdateUserRequest {
    @NotNull
    private Long id;
    @Email(message = "Некорректный email")
    private String email;
    private String login;
    private String name;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate birthday;

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return !(birthday == null);
    }

    public void checkCorrectData() {
        if (LocalDate.now().isBefore(birthday)) {
            log.error("Некорректная дата рождения");
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
