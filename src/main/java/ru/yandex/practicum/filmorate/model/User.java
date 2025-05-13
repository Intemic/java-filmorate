package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    @NonNull
    String email;
    @NonNull
    String login;
    String name;
    @NonNull
    @DateTimeFormat(style = "yyyy-MM-dd")
    LocalDate birthday;
}
