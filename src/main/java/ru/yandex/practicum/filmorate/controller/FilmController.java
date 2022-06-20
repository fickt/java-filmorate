package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< Updated upstream
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.datastorage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
=======
import ru.yandex.practicum.filmorate.datastorage.InMemoryFilmStorage;
>>>>>>> Stashed changes
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 Не получается аннотировать как @RestController, так как в файле конфигураций
 зависимостей уже определён как @Bean
*/

@RequestMapping("/films")
public class FilmController {
<<<<<<< Updated upstream
    private FilmStorage filmStorage;
    private FilmService filmService;
=======

    private InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
>>>>>>> Stashed changes
    private int filmIdGenerator = 0;
    private Logger filmControllerLogger = Logger.getLogger("filmControllerLogger");
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/add") //add
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        if (film == null || film.getName() == null || film.getName().isBlank() || film.getDescription().length() >= 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))
                || film.getDuration().isNegative()) {
            filmControllerLogger.log(Level.WARNING, "ValidationException /film/add");
            throw new ValidationException("Ошибка валидации /film/add");

        } else {
            filmControllerLogger.log(Level.INFO, "film has been added /add");
            film.setId(filmIdGenerator);
            filmStorage.addFilm(filmIdGenerator, film);
            filmIdGenerator++;
            return new ResponseEntity<>(film, HttpStatus.OK);
        }
    }

    @PutMapping("/update") //update
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


    @GetMapping("/allfilms") //allfilms
    public ResponseEntity<List> getAllFilms() {
        filmControllerLogger.log(Level.INFO, " /allfilms");
        return new ResponseEntity(filmStorage.getAllFilms().values(), HttpStatus.OK);
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilm(@PathVariable int filmId) {
        if (filmStorage.containsFilm(filmId)) {
            return new ResponseEntity<>(filmStorage.getAllFilms().get(filmId), HttpStatus.OK);
        } else {
            throw new NotFoundException("Film with" + filmId + "hasn't been found!");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> putLike(@PathVariable int id, @PathVariable int userId) {
        if(filmStorage.containsFilm(id)) {
            User user = restTemplate.getForObject("http://localhost:8080/users/finduser/" + userId, User.class);
            filmService.putLike(filmStorage.getAllFilms().get(id), user);
            filmControllerLogger.log(Level.INFO, "Like has been put! /like");
        } else {
            throw new NotFoundException("Film with" + id + "hasn't been found!");
        }
        return new ResponseEntity<>(filmStorage.getAllFilms().get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable int id, @PathVariable int userId) {
        if(filmStorage.containsFilm(id)) {
            User user = restTemplate.getForObject("http://localhost:8080/users/finduser/" + userId, User.class);
            filmService.removeLike(filmStorage.getAllFilms().get(id), user);
            filmControllerLogger.log(Level.INFO, "Like has been removed! /dislike");
        } else {
            throw new NotFoundException("Film with" + id + "hasn't been found!");
        }
        return new ResponseEntity<>(filmStorage.getAllFilms().get(id), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List> getTopFilms(@RequestParam int count) {
        if (!(filmStorage.getAllFilms().size() < count)) {
            return new ResponseEntity<>(filmService.getTopFilms(filmStorage, count), HttpStatus.OK);
        } else {
            throw new ValidationException("Required top:" + count + "but there are only: " +
                    filmStorage.getAllFilms().size() + "films in database");
        }
    }
}
