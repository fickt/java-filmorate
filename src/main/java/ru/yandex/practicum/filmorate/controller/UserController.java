package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.datastorage.DataStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/newuser")
    public User createNewUser(@RequestBody User user) {
        if (user.getEmail().isBlank() || user.getEmail() ==null || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {

            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            DataStorage.addUser(user.getId(), user);

        }
        return user;
    }

    @PutMapping("/updateuser")
    public User updateUser (@RequestBody User user) {
        if(DataStorage.containsUser(user.getId())) {
            DataStorage.addUser(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Ошибка валидации user/updateuser");
        }
    }

    @GetMapping("/allusers")
    public Map<Integer, User> getAllUsers() {
        return DataStorage.getAllUsers();
    }
}
