package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> filmStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    public void addFilm(long filmId, Film film) {
        filmStorage.put(filmId, film);
    }

    public boolean containsFilm(long filmId){
        return filmStorage.containsKey(filmId);
    }

    public Map<Long, Film> getAllFilms() {
        return filmStorage;
    }
}
