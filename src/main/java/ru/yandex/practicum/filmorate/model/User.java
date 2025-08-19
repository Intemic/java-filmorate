package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    private Long id;
    @NonNull
    @NotBlank
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate birthday;
    private Set<Long> friends = new LinkedHashSet<>();

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
