package ru.yandex.practicum.filmorate.datastorage.interfaces;



import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Repository
public interface FilmStorage {

    public void addFilm(long filmId, Film film);

    public boolean containsFilm(long filmId);

    public Map<Long, Film> getAllFilms();
}
