package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String login;
    private String name;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate birthday;
}
