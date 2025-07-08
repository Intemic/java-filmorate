package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testValidationCreateUser() {

        NewUserRequest userRequest = new NewUserRequest("@yandex.ru", "test",LocalDate.now().plusYears(1));
        ValidationException except = assertThrows(ValidationException.class,
                userRequest::checkCorrectData);
        assertEquals("Некорректная дата рождения", except.getMessage());
    }

    @Test
    public void testValidationCreateFilm() {
        NewFilmRequest filmRequest = new NewFilmRequest(LocalDate.of(1800, 01, 01), 100);
        ValidationException except = assertThrows(ValidationException.class,
                filmRequest::checkCorrectData);
        assertEquals("Дата не может быть ранее 28 декабря 1895 года", except.getMessage());
    }

}
