package ru.yandex.practicum.filmorate.datastorage;

import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

<<<<<<< Updated upstream:src/main/java/ru/yandex/practicum/filmorate/datastorage/InMemoryFilmStorage.java

=======
>>>>>>> Stashed changes:src/main/java/ru/yandex/practicum/filmorate/datastorage/FilmStorage.java
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> filmStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    public void addFilm(int filmId, Film film) {
        filmStorage.put(filmId, film);
    }

    public boolean containsFilm(int filmId){
        return filmStorage.containsKey(filmId);
    }

    public Map<Integer, Film> getAllFilms() {
        return filmStorage;
    }
}
