package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.datastorage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmStorage filmStorage = new FilmStorage();
    private int filmIdGenerator = 0;
    private Logger filmControllerLogger = Logger.getLogger("filmControllerLogger");

    @PostMapping() //add
    public Film addFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() >= 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))
                || film.getDuration().isNegative()) {
            filmControllerLogger.log(Level.WARNING, "ValidationException /film/add");
            throw new ValidationException("Ошибка валидации /film/add");

        } else {
            filmControllerLogger.log(Level.INFO, "film has been added /add");
            film.setId(filmIdGenerator);
            filmStorage.addFilm(filmIdGenerator, film);
            filmIdGenerator++;
            return film;
        }
    }

    @PutMapping() //update
    public Film updateFilm(@RequestBody Film film) {
        if (filmStorage.containsFilm(film.getId())) {
            filmStorage.addFilm(film.getId(), film);
            filmControllerLogger.log(Level.INFO, "film has been updated /update");
            return film;
        } else {
            filmControllerLogger.log(Level.WARNING, "ValidationException /film/update");
            throw new ValidationException("Ошибка валидации /film/update");
        }
    }

    @ResponseBody
    @GetMapping() //allfilms
    public ResponseEntity getAllFilms() {
        filmControllerLogger.log(Level.INFO, " /allfilms");
        return new ResponseEntity(filmStorage.getAllFilms().values(), HttpStatus.OK);
    }
}
