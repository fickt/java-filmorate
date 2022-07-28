package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    boolean containsFilm(long filmId);

    List<Film> getAllFilms();

    Film getFilm(long filmId);

    void updateFilm(Film film);

    void putLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getTopFilms(Integer count);

}
