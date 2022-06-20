package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public void putLike(Film film, User user) {

        film.getPersonsLikedFilm().add(user);
        film.updateAmountOfLikes();
    }

    public void removeLike(Film film, User user) {

        film.getPersonsLikedFilm().remove(user);
        film.updateAmountOfLikes();
    }

    public List<Film> getTopFilms(FilmStorage filmStorage, int count) {

        if(count == 0) {
            count = 10;
        }
       return filmStorage.getAllFilms().values()
                .stream()
                .limit(count)
                .sorted(Comparator.comparing(o -> o.getPersonsLikedFilm().size())).collect(Collectors.toList());
    }
}
