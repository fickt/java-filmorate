package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private UserService userService;
    private FilmStorage filmStorage;
    private Logger filmServiceLogger = Logger.getLogger("filmServiceLogger");
    private long filmIdGenerator = 1;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public ResponseEntity<Film> addFilm(Film film) {
        if (film == null || film.getName() == null || film.getName().isBlank() || film.getDescription().length() >= 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))
                || film.getDurationForComparing().isNegative() || film.getReleaseDate() == null) {
            filmServiceLogger.log(Level.WARNING, "ValidationException /film/add");
            throw new ValidationException("Ошибка валидации /film/add");

        } else {
            filmServiceLogger.log(Level.INFO, "film has been added /add");
            film.setId(filmIdGenerator);
            filmStorage.addFilm(filmIdGenerator, film);
            filmIdGenerator++;
            return new ResponseEntity<>(film, HttpStatus.OK);
        }
    }

    public ResponseEntity<Film> updateFilm(Film film) {
        if (filmStorage.containsFilm(film.getId())) {
            filmStorage.addFilm(film.getId(), film);
            filmServiceLogger.log(Level.INFO, "film has been updated /update");
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            filmServiceLogger.log(Level.WARNING, "NotFoundException /film/update");
            throw new NotFoundException("Film with" + film.getId() + "ID hasn't been found! /film/update");
        }
    }

    public ResponseEntity<List<Film>> getAllFilms() {
        filmServiceLogger.log(Level.INFO, " /allfilms");
        return new ResponseEntity(filmStorage.getAllFilms().values(), HttpStatus.OK);
    }

    public ResponseEntity<Film> getFilm(long filmId) {
        if (filmStorage.containsFilm(filmId)) {
            filmServiceLogger.log(Level.INFO, " /getFilm");
            return new ResponseEntity<>(filmStorage.getAllFilms().get(filmId), HttpStatus.OK);
        } else {
            throw new NotFoundException("Film with" + filmId + "hasn't been found!");
        }
    }

    public ResponseEntity<Film> putLike(long filmId, long userId) {

        if (userId < 0) {
            filmServiceLogger.log(Level.INFO, "ошибка валидации, userID - отрицательное число /like");
            throw new ValidationException("ошибка валидации, userID - отрицательное число");
        } else {
            if (filmStorage.containsFilm(filmId)) {
                User user = userService.getUser(userId).getBody();
                filmStorage.getAllFilms().get(filmId).getPersonsLikedFilm().add(user.getId());
                filmStorage.getAllFilms().get(filmId).updateAmountOfLikes();
                filmServiceLogger.log(Level.INFO, "Like has been put! /like");
                return new ResponseEntity(filmStorage.getAllFilms().get(filmId), HttpStatus.OK);
            } else {
                throw new NotFoundException("Film with" + filmId + "hasn't been found!");
            }
        }

    }

    public ResponseEntity<Film> removeLike(long filmId, long userId) {

        if (userId < 0 || filmId < 0) {
            filmServiceLogger.log(Level.INFO, "ошибка валидации, userID / filmID - отрицательное число /like");
            throw new NotFoundException("ошибка валидации, userID / filmID- отрицательное число");
        } else {
            if (filmStorage.containsFilm(filmId)) {
                User user = userService.getUser(userId).getBody();
                filmStorage.getAllFilms().get(filmId).getPersonsLikedFilm().remove(user.getId());
                filmStorage.getAllFilms().get(filmId).updateAmountOfLikes();
                filmServiceLogger.log(Level.INFO, "Like has been removed! /dislike");
                return new ResponseEntity(filmStorage.getAllFilms().get(filmId), HttpStatus.OK);
            } else {
                throw new NotFoundException("Film with" + filmId + "hasn't been found!");
            }
        }
    }

    public ResponseEntity<List<Film>> getTopFilms(Integer count) {

        return new ResponseEntity<>(filmStorage.getAllFilms().values()
                .stream()
                .limit(count)
                .sorted(Comparator.comparing(o -> o.getRate())).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

}
