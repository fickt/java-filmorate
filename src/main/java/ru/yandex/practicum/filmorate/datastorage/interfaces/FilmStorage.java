package ru.yandex.practicum.filmorate.datastorage.interfaces;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {

    void addFilm(Film film);

    boolean containsFilm(long filmId);

    List<Film> getAllFilms();

    Film getFilm(long filmId);

}
