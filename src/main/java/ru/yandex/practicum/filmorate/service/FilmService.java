package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
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
            filmStorage.addFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        }
    }

    public ResponseEntity<Film> updateFilm(Film film) {

        if (!(filmStorage instanceof FilmDbStorage)) { // если реализация InMemoryFilmStorage
            if (filmStorage.containsFilm(film.getId())) {
                filmStorage.addFilm(film);
                filmServiceLogger.log(Level.INFO, "film has been updated /update");
                return new ResponseEntity<>(film, HttpStatus.OK);
            } else {
                filmServiceLogger.log(Level.WARNING, "NotFoundException /film/update");
                throw new NotFoundException("Film with" + film.getId() + "ID hasn't been found! /film/update");
            }
        } else { // если реализация FilmDbStorage

            if (filmStorage.containsFilm(film.getId())) {
                ((FilmDbStorage) filmStorage).getJdbcTemplate().update("UPDATE FILM_TABLE SET NAME=?, DESCRIPTION = ?," +
                                "DURATION =?, RELEASE_DATE = ?, RATING_ID = ? WHERE ID = ?",
                        film.getName(),
                        film.getDescription(),
                        film.getDuration(),
                        film.getReleaseDate(),
                        film.getMpa().getId(),
                        film.getId());
                ((FilmDbStorage) filmStorage).getJdbcTemplate()
                        .update("DELETE  FROM GENRE_FILM_TABLE WHERE FILM_ID=? ", film.getId());
                for (Genre genre : film.getGenres()) {
                    try {
                        ((FilmDbStorage) filmStorage).getJdbcTemplate()
                                .update("INSERT INTO GENRE_FILM_TABLE (FILM_ID, GENRE_ID)  VALUES (?,?)", film.getId(), genre.getId());
                    } catch (DataIntegrityViolationException e) {
                        continue;
                    }

                }
                return new ResponseEntity<>(filmStorage.getFilm(film.getId()), HttpStatus.OK);
            } else {
                filmServiceLogger.log(Level.WARNING, "NotFoundException /film/update");
                throw new NotFoundException("Film with" + film.getId() + "ID hasn't been found! /film/update");
            }
        }
    }

    public ResponseEntity<List<Film>> getAllFilms() {
        filmServiceLogger.log(Level.INFO, " /allfilms");
        return new ResponseEntity(filmStorage.getAllFilms(), HttpStatus.OK);
    }

    public ResponseEntity<Film> getFilm(long filmId) {

            if (filmStorage.containsFilm(filmId)) {
                filmServiceLogger.log(Level.INFO, " /getFilm");
                return new ResponseEntity<>(filmStorage.getFilm(filmId), HttpStatus.OK);
            } else {
                throw new NotFoundException("Film with" + filmId + "hasn't been found!");
            }
        }



    public ResponseEntity<Film> putLike(long filmId, long userId) {

        if (!(filmStorage instanceof FilmDbStorage)) { // если реализация InMemoryFilmStorage
            if (userId < 0) {
                filmServiceLogger.log(Level.INFO, "ошибка валидации, userID - отрицательное число /like");
                throw new ValidationException("ошибка валидации, userID - отрицательное число");
            } else {
                if (filmStorage.containsFilm(filmId)) {
                    User user = userService.getUser(userId).getBody();
                    filmStorage.getFilm(filmId).getPersonsLikedFilm().add(user.getId());
                    filmStorage.getFilm(filmId).updateAmountOfLikes();
                    filmServiceLogger.log(Level.INFO, "Like has been put! /like");
                    return new ResponseEntity(filmStorage.getFilm(filmId), HttpStatus.OK);
                } else {
                    throw new NotFoundException("Film with" + filmId + "hasn't been found!");
                }
            }

        } else { // если реализация FilmDbStorage

            if (userId < 0) {
                filmServiceLogger.log(Level.INFO, "ошибка валидации, userID - отрицательное число /like");
                throw new ValidationException("ошибка валидации, userID - отрицательное число");
            } else {
                if (filmStorage.containsFilm(filmId)) {
                    ((FilmDbStorage) filmStorage).getJdbcTemplate().update("INSERT INTO LIKE_USER_TABLE (FILM_ID, USER_ID)" +
                            "VALUES (?,?)", filmId, userId);

                    int rate = ((FilmDbStorage) filmStorage).getJdbcTemplate().queryForObject("SELECT COUNT(FILM_ID) " +
                            "FROM LIKE_USER_TABLE WHERE FILM_ID = " + filmId, Integer.class);

                    ((FilmDbStorage) filmStorage).updateRate(filmId, rate);
                    filmServiceLogger.log(Level.INFO, "Like has been put! /like");
                    return new ResponseEntity(filmStorage.getFilm(filmId), HttpStatus.OK);
                } else {
                    throw new NotFoundException("Film with" + filmId + "hasn't been found!");
                }
            }
        }
    }

    public ResponseEntity<Film> removeLike(long filmId, long userId) {

        if (!(filmStorage instanceof FilmDbStorage)) { // если реализация InMemoryFilmStorage
            if (userId < 0 || filmId < 0) {
                filmServiceLogger.log(Level.INFO, "ошибка валидации, userID / filmID - отрицательное число /like");
                throw new NotFoundException("ошибка валидации, userID / filmID- отрицательное число");
            } else {
                if (filmStorage.containsFilm(filmId)) {
                    User user = userService.getUser(userId).getBody();
                    filmStorage.getFilm(filmId).getPersonsLikedFilm().remove(user.getId());
                    filmStorage.getFilm(filmId).updateAmountOfLikes();
                    filmServiceLogger.log(Level.INFO, "Like has been removed! /dislike");
                    return new ResponseEntity(filmStorage.getFilm(filmId), HttpStatus.OK);
                } else {
                    throw new NotFoundException("Film with" + filmId + "hasn't been found!");
                }
            }

        } else { // если реализация FilmDbStorage

            if (userId < 0 || filmId < 0) {
                filmServiceLogger.log(Level.INFO, "ошибка валидации, userID / filmID - отрицательное число /like");
                throw new NotFoundException("ошибка валидации, userID / filmID- отрицательное число");
            } else {
                ((FilmDbStorage) filmStorage).getJdbcTemplate().update("DELETE FROM LIKE_USER_TABLE WHERE FILM_ID=? AND " +
                        "USER_ID=?", filmId, userId);
                int rate = ((FilmDbStorage) filmStorage).getJdbcTemplate().queryForObject("SELECT COUNT(FILM_ID) " +
                        "FROM LIKE_USER_TABLE WHERE FILM_ID = " + filmId, Integer.class);
                ((FilmDbStorage) filmStorage).updateRate(filmId, rate);
                return new ResponseEntity(filmStorage.getFilm(filmId), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<List<Film>> getTopFilms(Integer count) {
        return new ResponseEntity<>(filmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparing(o -> o.getRate()))
                .limit(count)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
