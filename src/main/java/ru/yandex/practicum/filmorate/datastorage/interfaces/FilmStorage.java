package ru.yandex.practicum.filmorate.datastorage.interfaces;


import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Repository
public interface FilmStorage {

    void addFilm(long filmId, Film film);

    boolean containsFilm(long filmId);

    Map<Long, Film> getAllFilms();
}
