package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.datastorage.DataStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {

    @PostMapping("/add")
    public Film addFilm(@RequestBody Film film) {
        if (film.getName().isBlank() || film.getName() == null || film.getDescription().length() >= 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))
                || film.getDuration().isNegative()) {

            throw new ValidationException("Ошибка валидации /film/add");

        } else {
            DataStorage.addFilm(film.getId(), film);
            return film;
        }
    }

    @PutMapping("/update")
    public Film updateFilm(@RequestBody Film film) {
        if(DataStorage.containsFilm(film.getId())) {
            DataStorage.addFilm(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Ошибка валидации /film/update");
        }
    }

    @GetMapping("/allfilms")
    public Map<Integer, Film> getAllFilms() {
        return DataStorage.getAllFilms();
    }
}
