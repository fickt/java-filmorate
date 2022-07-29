package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long filmIdGenerator = 1;

    private Map<Long, Film> filmStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    public Film addFilm(Film film) {
        filmStorage.put(filmIdGenerator, film);
        filmIdGenerator++;
        return filmStorage.get(filmIdGenerator - 1);
    }

    @Override
    public boolean containsFilm(long filmId) {
        return filmStorage.containsKey(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.values();
    }

    @Override
    public Film getFilm(long filmId) {
        return filmStorage.get(filmId);
    }

    @Override
    public void updateFilm(Film film) {
        filmStorage.put(film.getId(), film);
    }

    @Override
    public void putLike(long filmId, long userId) {
        filmStorage.get(filmId).getPersonsLikedFilm().add(userId);
        filmStorage.get(filmId).updateAmountOfLikes();
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        filmStorage.get(filmId).getPersonsLikedFilm().remove(userId);
        filmStorage.get(filmId).updateAmountOfLikes();
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return getAllFilms()
                .stream()
                .sorted(Comparator.comparing(o -> o.getRate()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
