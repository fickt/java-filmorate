package ru.yandex.practicum.filmorate.datastorage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public class DataStorage {
   static Map<Integer, User> userStorage = new HashMap<>();
   static Map<Integer, Film> filmStorage = new HashMap<>();

    public static void addFilm(int filmId, Film film) {
        filmStorage.put(filmId, film);
    }

    public static boolean containsFilm(int filmId){
        return filmStorage.containsKey(filmId);
    }

    public static Map<Integer, Film> getAllFilms() {
        return filmStorage;
    }

    public static void addUser (int userId, User user) {
         userStorage.put(userId, user);
    }

    public static boolean containsUser(int userId) {
       return userStorage.containsKey(userId);
    }

    public static Map<Integer, User> getAllUsers() {
        return userStorage;
    }
}
