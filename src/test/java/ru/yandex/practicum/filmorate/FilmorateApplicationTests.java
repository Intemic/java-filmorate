package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utill.Mode;

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
        UserController controller = new UserController();
        User user = new User("@yandex.ru", "test", LocalDate.of(1972, 05, 15));
        user.setId(controller.getNextId());

        ValidationException except = assertThrows(ValidationException.class,
                () -> controller.checkData(user, Mode.CREATE));
        assertEquals("Некорректный email", except.getMessage());


        user.setEmail("test@yandex.ru");
        controller.users.put(user.getId(), user);

        DuplicatedDataException exceptDublicate = assertThrows(DuplicatedDataException.class,
                () -> controller.checkData(user, Mode.CREATE));
        assertEquals("Этот email уже используется", exceptDublicate.getMessage());


        User user2 = new User("test2@yandex.ru", "", LocalDate.of(1972, 05, 15));
        user2.setId(controller.getNextId());

        except = assertThrows(ValidationException.class,
                () -> controller.checkData(user2, Mode.CREATE));
        assertEquals("Некорректный login", except.getMessage());

        user2.setLogin("test2");
        user2.setBirthday(LocalDate.now().plusYears(1));
        except = assertThrows(ValidationException.class,
                () -> controller.checkData(user2, Mode.CREATE));
        assertEquals("Некорректная дата рождения", except.getMessage());

        user2.setBirthday(LocalDate.now().minusYears(10));
        assertEquals(user2, controller.checkData(user2, Mode.CREATE));
    }

    @Test
    public void testValidationUpdateUser() {
        UserController controller = new UserController();
        User user = new User("test@yandex.ru", "test", LocalDate.of(1972, 05, 15));
        user.setId(controller.getNextId());

        controller.users.put(user.getId(), user);

        User user2 = new User("test2@yandex.ru", "test2", LocalDate.of(1972, 05, 15));

        ValidationException except = assertThrows(ValidationException.class,
                () -> controller.checkData(user2, Mode.UPDATE));
        assertEquals("ID пользователя не указан", except.getMessage());

        user2.setId(50L);
        except = assertThrows(ValidationException.class,
                () -> controller.checkData(user2, Mode.UPDATE));
        assertEquals("Пользователь не найден", except.getMessage());

        user2.setId(user.getId());
        assertEquals(user, controller.checkData(user2, Mode.UPDATE));
    }

    @Test
    public void testValidationCreateFilm() {
        char[] allChars = IntStream.concat(
                        IntStream.rangeClosed('0', '9'),
                        IntStream.rangeClosed('A', 'Z'))
                .mapToObj(c -> (char) c + "").collect(Collectors.joining()).toCharArray();

        FilmController controller = new FilmController();
        Film film = new Film("", LocalDate.of(2020, 01, 15), 160);

        ValidationException except = assertThrows(ValidationException.class,
                () -> controller.checkData(film, Mode.CREATE));
        assertEquals("Название не может быть пустым", except.getMessage());

        film.setName("Просто фильм");

        StringBuffer buffer = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 250; i++)
            buffer.append(rand.nextInt(allChars.length));

        film.setDescription(buffer.toString());

        except = assertThrows(ValidationException.class,
                () -> controller.checkData(film, Mode.CREATE));
        assertEquals("Длинна не может быть больше 200 символов", except.getMessage());

        film.setDescription("Просто описание");
        film.setReleaseDate(LocalDate.of(1800, 01, 01));
        except = assertThrows(ValidationException.class,
                () -> controller.checkData(film, Mode.CREATE));
        assertEquals("Дата не может быть ранее 28 декабря 1895 года", except.getMessage());

        film.setReleaseDate(LocalDate.now());
        film.setDuration(-5);
        except = assertThrows(ValidationException.class,
                () -> controller.checkData(film, Mode.CREATE));
        assertEquals("Длительность должна быть положительным числом", except.getMessage());
    }

    @Test
    public void testValidationUpdateFilm() {
        FilmController controller = new FilmController();
        Film film = new Film("Просто фильм", LocalDate.of(2020, 01, 15), 160);
        film.setDescription("Описание фильма");
        film.setId(controller.getNextId());

        controller.films.put(film.getId(), film);

        Film film2 = new Film("Просто фильм 2", LocalDate.of(2024, 01, 15), 200);
        film2.setDescription("Описание фильма 2");

        ValidationException except = assertThrows(ValidationException.class,
                () -> controller.checkData(film2, Mode.UPDATE));
        assertEquals("ID фильма не указан", except.getMessage());

        film2.setId(50L);
        except = assertThrows(ValidationException.class,
                () -> controller.checkData(film2, Mode.UPDATE));
        assertEquals("Фильм не найден", except.getMessage());

        film2.setId(film.getId());
        assertEquals(film, controller.checkData(film2, Mode.UPDATE));
    }

}
