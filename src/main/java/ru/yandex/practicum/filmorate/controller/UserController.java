package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< Updated upstream
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
=======
import ru.yandex.practicum.filmorate.datastorage.InMemoryUserStorage;
>>>>>>> Stashed changes
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 Не получается аннотировать как @RestController, так как в файле конфигураций
 зависимостей уже определён как @Bean
 */

@RequestMapping("/users")
public class UserController {

<<<<<<< Updated upstream
    private UserStorage userStorage;
=======
    private InMemoryUserStorage userStorage = new InMemoryUserStorage();
>>>>>>> Stashed changes
    private int userIdGenerator = 0;
    private Logger userControllerLogger = Logger.getLogger("userControllerLogger");
    private UserService userService;
    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping("/newuser") //newuser
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            userControllerLogger.log(Level.WARNING, "ValidationException /newuser");
            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(userIdGenerator);
            userStorage.addUser(userIdGenerator, user);
            userIdGenerator++;
            userControllerLogger.log(Level.INFO, "user has been created! /newuser");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update") //updateuser
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

    @GetMapping("/allusers") //allusers
    @ResponseBody
    public ResponseEntity getAllUsers() {
        userControllerLogger.log(Level.INFO, " /allusers");
        return new ResponseEntity(userStorage.getAllUsers().values(), HttpStatus.OK);
    }

    @GetMapping("/finduser/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId) {
        userControllerLogger.log(Level.INFO, "/allusers");
        if (userStorage.getAllUsers().containsKey(userId)) {
            userControllerLogger.log(Level.INFO, "successfully /finduser");
            return new ResponseEntity<>(userStorage.getAllUsers().get(userId), HttpStatus.OK);
        } else {
            userControllerLogger.log(Level.WARNING, "User with given ID hasn't been found! /finduser");
            throw new NotFoundException("User with given ID hasn't been found!");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            userService.addFriend(userStorage.getAllUsers().get(id), userStorage.getAllUsers().get(friendId));
            userControllerLogger.log(Level.INFO, "successfully /friends");
            return new ResponseEntity<>(userStorage.getAllUsers().get(id), HttpStatus.OK);
        } else {
            userControllerLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            userService.deleteFriend(userStorage.getAllUsers().get(id), userStorage.getAllUsers().get(friendId));
            userControllerLogger.log(Level.INFO, "successfully /friends DELETE");
            return new ResponseEntity<>(userStorage.getAllUsers().get(id), HttpStatus.OK);
        } else {
            userControllerLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends DELETE");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<User>> getAllFriendsOfUser(@PathVariable int id) {
        if (userStorage.containsUser(id)) {
            userControllerLogger.log(Level.INFO, "successfully /allfriends");
         return new ResponseEntity<>(userService.getAllFriendsOfUser(userStorage.getAllUsers().get(id)), HttpStatus.OK);
        } else {
            userControllerLogger.log(Level.INFO, "User with given ID hasn't been found! /allfriends");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (userStorage.containsUser(id) && userStorage.containsUser(otherId)) {
            userControllerLogger.log(Level.INFO, "successfully /friends/common");
            return new ResponseEntity<>(userService.findCommonFriends(userStorage.getAllUsers().get(id),
                    userStorage.getAllUsers().get(otherId)), HttpStatus.OK);
        } else {
            userControllerLogger.log(Level.INFO, "User with given ID hasn't been found! /friends/common");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }
}
