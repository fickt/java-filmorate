package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FilmorateApplication.class},  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

@ActiveProfiles("test")

class FilmorateApplicationTests {

    @Test
    void shouldPutOneLikeToFilm() {
        RestTemplate restTemplate = new RestTemplate();
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(2002, 12,3));
        film.setDuration(Duration.of(200, ChronoUnit.SECONDS));
        film.setDescription("description");

        User user = new User();
        user.setName("name");
        user.setEmail("login@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        restTemplate.postForObject("http://localhost:8080/films", film, Film.class);

        restTemplate.postForObject("http://localhost:8080/users", user, User.class);

        restTemplate.put("http://localhost:8080/films/0/like/0", Film.class);

        int likes = restTemplate.getForObject("http://localhost:8080/films/0", Film.class).getRate();

        assertEquals(1, likes);
    }

}
