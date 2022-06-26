package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    public FilmController() {

    }

    @PostMapping //add
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {

        return filmService.addFilm(film);
    }

    @PutMapping //update
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {

        return filmService.updateFilm(film);
    }


    @GetMapping //allfilms
    public ResponseEntity<List<Film>> getAllFilms() {

        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilm(@PathVariable long filmId) {

        return filmService.getFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> putLike(@PathVariable long id, @PathVariable long userId) {

        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable long id, @PathVariable long userId) {

        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular") //?count={count}
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(required = false) Integer count) {
        if(count == null || count == 0 || count < 0) {
            count = 10;
        }
        return filmService.getTopFilms(count);
    }
}
