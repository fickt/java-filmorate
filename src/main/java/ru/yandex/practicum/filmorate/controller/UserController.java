package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.datastorage.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/users")
public class UserController {

    private UserStorage userStorage = new UserStorage();
    private int userIdGenerator = 0;
    private Logger userControllerLogger = Logger.getLogger("userControllerLogger");
    @PostMapping("/newuser")
    public User createNewUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            userControllerLogger.log(Level.WARNING, "ValidationException /newuser");
            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userStorage.addUser(userIdGenerator, user);
            userIdGenerator++;
            userControllerLogger.log(Level.INFO, "user has been created! /newuser");
        }
        return user;
    }

    @PutMapping("/updateuser")
    public User updateUser(@RequestBody User user) {
        if (userStorage.containsUser(user.getId())) {
            userStorage.addUser(user.getId(), user);
            userControllerLogger.log(Level.INFO, "user has been updated! /updateuser");
            return user;
        } else {
            Logger.getLogger("logger").log(Level.WARNING, "ValidationException /updateuser");
            throw new ValidationException("Ошибка валидации user/updateuser");
        }
    }

    @GetMapping("/allusers")
    @ResponseBody
    public ResponseEntity getAllUsers() {
        userControllerLogger.log(Level.INFO, " /allusers");
        return new ResponseEntity(userStorage.getAllUsers().values(), HttpStatus.OK);
    }
}
