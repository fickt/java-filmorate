package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long filmIdGenerator = 1;

    private Map<Long, Film> filmStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    public void addFilm(Film film) {
        filmStorage.put(filmIdGenerator, film);
        filmIdGenerator++;
    }

    public boolean containsFilm(long filmId){
        return filmStorage.containsKey(filmId);
    }

    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.values();
    }

    public Film getFilm(long filmId) {
        return filmStorage.get(filmId);
    }

}
