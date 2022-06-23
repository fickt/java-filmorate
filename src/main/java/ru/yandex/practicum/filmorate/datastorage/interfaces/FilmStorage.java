package ru.yandex.practicum.filmorate.datastorage.interfaces;



import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Component
public interface FilmStorage {

    public void addFilm(int filmId, Film film);

    public boolean containsFilm(int filmId);

    public Map<Integer, Film> getAllFilms();
}
