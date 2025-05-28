package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testValidationCreateUser() {
        User user = new User("@yandex.ru", "test", LocalDate.of(1972, 05, 15));
        user.setId(1L);

        ValidationException except = assertThrows(ValidationException.class,
                () -> user.checkCorrectData());
        assertEquals("Некорректный email", except.getMessage());

        user.setEmail("test@yandex.ru");

        User user2 = new User("test2@yandex.ru", "", LocalDate.of(1972, 05, 15));
        user2.setId(2L);

        except = assertThrows(ValidationException.class,
                () -> user2.checkCorrectData());
        assertEquals("Некорректный login", except.getMessage());

        user2.setLogin("test2");
        user2.setBirthday(LocalDate.now().plusYears(1));
        except = assertThrows(ValidationException.class,
                () -> user2.checkCorrectData());
        assertEquals("Некорректная дата рождения", except.getMessage());

    }

    @Test
    public void testValidationCreateFilm() {
        char[] allChars = IntStream.concat(
                        IntStream.rangeClosed('0', '9'),
                        IntStream.rangeClosed('A', 'Z'))
                .mapToObj(c -> (char) c + "").collect(Collectors.joining()).toCharArray();

        Film film = new Film("", LocalDate.of(2020, 01, 15), 160);
        film.setId(1L);

        ValidationException except = assertThrows(ValidationException.class,
                () -> film.checkCorrectData());
        assertEquals("Название не может быть пустым", except.getMessage());

        film.setName("Просто фильм");

        StringBuffer buffer = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 250; i++)
            buffer.append(rand.nextInt(allChars.length));

        film.setDescription(buffer.toString());

        except = assertThrows(ValidationException.class,
                () -> film.checkCorrectData());
        assertEquals("Длинна не может быть больше 200 символов", except.getMessage());

        film.setDescription("Просто описание");
        film.setReleaseDate(LocalDate.of(1800, 01, 01));
        except = assertThrows(ValidationException.class,
                () -> film.checkCorrectData());
        assertEquals("Дата не может быть ранее 28 декабря 1895 года", except.getMessage());

        film.setReleaseDate(LocalDate.now());
        film.setDuration(-5);
        except = assertThrows(ValidationException.class,
                () -> film.checkCorrectData());
        assertEquals("Длительность должна быть положительным числом", except.getMessage());
    }

}
