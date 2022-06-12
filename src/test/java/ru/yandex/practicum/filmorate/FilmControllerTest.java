package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mvc;
    private Gson json = new Gson();

    @Test
    void shouldReturnStatus400CreatingFilmWithMissingName() throws Exception {
        Film film = new Film();
        film.setId(0);
        film.setName("");
        film.setReleaseDate(LocalDate.of(2002, 12,3));
        film.setDuration(Duration.of(100, ChronoUnit.MINUTES));
        film.setDescription("description of film");

        int responseStatus = this.mvc.perform(post("/films/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(film))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnStatus400CreatingFilmWithDescriptionOver200length() throws Exception {
        Film film = new Film();
        film.setId(0);
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(2002, 12,3));
        film.setDuration(Duration.of(100, ChronoUnit.SECONDS));
        film.setDescription("There is a description over 200 symbols, therefore test shouldn't be passed," +
                "otherwise I will have to work on the program logic over and over again and there are no " +
                "appropriate words to find to describe how strong I dont want to do this............................" +
                ".................................................................................................");
        int responseStatus = this.mvc.perform(post("/films/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(film))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnStatus400CreatingFilmWithInvalidReleaseDate() throws Exception {
        Film film = new Film();
        film.setId(0);
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(Duration.of(100, ChronoUnit.SECONDS));
        film.setDescription("description");

        int responseStatus = this.mvc.perform(post("/films/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(film))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnStatus400CreatingFilmWithNegativeDuration() throws Exception {
        Film film = new Film();
        film.setId(0);
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(2002, 12,3));
        film.setDuration(Duration.of(-100, ChronoUnit.SECONDS));
        film.setDescription("description");

        int responseStatus = this.mvc.perform(post("/films/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(film))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnStatus200CreatingFilmWithValidValues() throws Exception {
        Film film = new Film();
        film.setId(0);
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(2002, 12,3));
        film.setDuration(Duration.of(200, ChronoUnit.SECONDS));
        film.setDescription("description");

        int responseStatus = this.mvc.perform(post("/films/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(film))).andReturn().getResponse().getStatus();

        assertEquals(200, responseStatus);
    }
}
