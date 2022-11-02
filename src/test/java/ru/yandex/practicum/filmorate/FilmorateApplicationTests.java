package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.datastorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.datastorage.GenreDao;
import ru.yandex.practicum.filmorate.datastorage.MpaDao;
import ru.yandex.practicum.filmorate.datastorage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan
class FilmorateApplicationTests {


    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;

    private final MpaDao mpaDao;

    private final GenreDao genreDao;

    private void createUser() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));

        Optional<User> userOptional = Optional.ofNullable(userStorage.addUser(user));

        assertThat(userOptional)
                .isPresent();
    }

    private void createFilm() {
        Film film = new Film();
        film.setName("Green Elephant");
        film.setDescription("Eine lehrreiche Geschichte, warum man den Kontakt mit den Verrückten vermeiden muss");
        film.setReleaseDateAsString("1999-01-01");
        film.setDurationAsLong(120L);
        film.setMpa(mpaDao.getMpa(1).getBody());
        film.getGenres().add(genreDao.getGenre(1).getBody());
        filmStorage.addFilm(film);
    }

    @Test
    void shouldPutOneLikeAndReturnFilmWithRateOne() {
        createFilm();
        createUser();

        filmStorage.putLike(1, 1);
        assertEquals(filmStorage.getFilm(1).getRate(), 1);
    }

    @Test
    void shouldRemoveOneLikeAndReturnFilmWithRateZero() {
        createFilm();
        createUser();

        filmStorage.putLike(1, 1);
        filmStorage.deleteLike(1,1);
        assertEquals(filmStorage.getFilm(1).getRate(),0);
    }

    @Test
    void shouldUpdateFilmGenre() {
        createFilm();
        Film film = filmStorage.getFilm(1);
        film.getGenres().add(genreDao.getGenre(2).getBody());
        film.getGenres().add(genreDao.getGenre(3).getBody());
        filmStorage.updateFilm(film);
        Film filmUpdated = filmStorage.getFilm(1);
        assertEquals(filmUpdated.getGenres().size(), 3);
    }

    @Test
    void shouldUpdateFilmMpa() {
        createFilm();
        Film film = filmStorage.getFilm(1);
        film.setMpa(mpaDao.getMpa(4).getBody());
        filmStorage.updateFilm(film);
        Film filmUpdated = filmStorage.getFilm(1);
        assertEquals(filmUpdated.getMpa().getId(), 4);
    }
}