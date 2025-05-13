package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    @NonNull
    @Email
    @NotBlank
    String email;
    @NonNull
    @NotBlank
    String login;
    String name;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    LocalDate birthday;
}
