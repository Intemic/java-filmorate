package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.UserCreate;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FilmDbStorage.class, FilmRowMapper.class})
//@SpringBootTest
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private User user;
    private Film film;

    private String getRandomWorlds(int len) {
        StringBuilder stringBuilder = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < len; i++)
            stringBuilder.append(Character.toChars(rand.nextInt(58) + 65));

        return stringBuilder.toString();
    }

    private User createUser() {
        User user = new User();
        user.setEmail(getRandomWorlds(15) + "@yandex.ru");
        user.setLogin(getRandomWorlds(15));
        user.setName(getRandomWorlds(15));
        user.setBirthday(LocalDate.now());
        return user;
    }

    private Film createFilm() {
        Film film = new Film();
        Random rand = new Random();
        film.setName(getRandomWorlds(20));
        film.setDescription(getRandomWorlds(30));
        film.setReleaseDate(LocalDate.now().plusDays((rand.nextInt(30))));
        film.setDuration(rand.nextInt(500) + 1);
        return film;
    }

    @BeforeEach
    public void beforeTest() {
        user = createUser();
        film = createFilm();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testValidationCreateUser() {

        UserCreate userRequest = new UserCreate("@yandex.ru", "test", LocalDate.now().plusYears(1));
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


    @Test
    public void testCreateUser() {
        User createUser = userStorage.create(user);
        assertNotEquals(0, createUser.getId());
    }

    @Test
    public void testUpdateUser() {
        User createUser = userStorage.create(user);
        createUser.setName("update_name");
        createUser.setLogin("update_login");
        User changedUser = userStorage.update(createUser);
        assertEquals("update_name", changedUser.getName());
        assertEquals("update_login", changedUser.getLogin());
    }

    @Test
    public void testDeleteUser() {
        User firstUser = userStorage.create(user);
        User secondUser = userStorage.create(createUser());
        Collection<User> users = userStorage.getAll();
        assertEquals(2, users.size());
        userStorage.delete(secondUser.getId());
        users = userStorage.getAll();
        assertEquals(1, users.size());
        Optional<User> userOptional = userStorage.get(firstUser.getId());
        assertTrue(userOptional.isPresent());
        assertEquals(firstUser.getId(), userOptional.get().getId());
    }

    @Test
    public void testFindUserById() {
        User firstUser = userStorage.create(user);
        User secondUser = userStorage.create(createUser());

        Optional<User> userOptional = userStorage.get(firstUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", firstUser.getId())
                );
    }

    @Test
    public void testGetAllUsers() {
        User firstUser = userStorage.create(user);
        User secondUser = userStorage.create(createUser());
        Collection<User> users = userStorage.getAll();
        assertEquals(2, users.size());
        assertTrue(users.contains(firstUser));
        assertTrue(users.contains(secondUser));
    }

    @Test
    public void testCreateFilm() {
        Film createdFilm = filmStorage.create(film);
        assertNotEquals(0, createdFilm.getId());
    }

    @Test
    public void testUpdateFilm() {
        Film createdFilm = filmStorage.create(film);
        createdFilm.setName("update_name");
        createdFilm.setDescription("update_description");
        Film changedFilm = filmStorage.update(createdFilm);
        assertEquals("update_name", changedFilm.getName());
        assertEquals("update_description", changedFilm.getDescription());
    }

    @Test
    public void testDeleteFilm() {
        Film firstFilm = filmStorage.create(film);
        Film secondFilm = filmStorage.create(createFilm());
        Collection<Film> films = filmStorage.getAll();
        assertEquals(2, films.size());
        filmStorage.delete(secondFilm.getId());
        films = filmStorage.getAll();
        assertEquals(1, films.size());
        Optional<Film> filmOptional = filmStorage.get(firstFilm.getId());
        assertTrue(filmOptional.isPresent());
        assertEquals(firstFilm.getId(), filmOptional.get().getId());
    }

    @Test
    public void testGetAllFilm() {
        Film firstFilm = filmStorage.create(film);
        Film secondFilm = filmStorage.create(createFilm());
        Collection<Film> films = filmStorage.getAll();
        assertEquals(2, films.size());
        assertTrue(films.contains(firstFilm));
        assertTrue(films.contains(secondFilm));
    }

    @Test
    public void testFindFilmById() {
        Film firstFilm = filmStorage.create(film);
        Film secondFilm = filmStorage.create(createFilm());

        Optional<Film> filmOptional = filmStorage.get(firstFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", firstFilm.getId())
                );
    }
}
