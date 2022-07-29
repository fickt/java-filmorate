package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class UserService {

    private UserStorage userStorage;
    private Logger userServiceLogger = Logger.getLogger("userServiceLogger");

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            userServiceLogger.log(Level.WARNING, "ValidationException /newuser");
            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userServiceLogger.log(Level.INFO, "user has been created! /newuser");
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.containsUser(user.getId())) {
            userStorage.updateUser(user);
            userServiceLogger.log(Level.INFO, "user has been updated! /updateuser");
            return user;
        } else {
            Logger.getLogger("logger").log(Level.WARNING, "ValidationException /updateuser " + user.getId());
            throw new NotFoundException("Ошибка валидации user /updateuser");
        }
    }

    public java.util.Collection<User> getAllUsers() {
        userServiceLogger.log(Level.INFO, " /allusers");
        return userStorage.getAllUsers();
    }

    public User getUser(long userId) {
        userServiceLogger.log(Level.INFO, "/finduser");
        if (userStorage.containsUser(userId)) {
            userServiceLogger.log(Level.INFO, "successfully /finduser");
           return userStorage.getUser(userId);
        } else {
            userServiceLogger.log(Level.WARNING, "User with " + userId + " ID hasn't been found! /finduser ");
            throw new NotFoundException("User with given ID hasn't been found!");
        }
    }

    public User addFriend(long userId, long friendId) {
        if (userStorage.containsUser(userId) && userStorage.containsUser(friendId)) {
            userStorage.addFriend(userId, friendId);
            userServiceLogger.log(Level.INFO, "successfully /friends");
            return userStorage.getUser(userId);
        } else {
            userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    public User deleteFriend(long userId, long friendId) {
        if (userStorage.containsUser(userId) && userStorage.containsUser(friendId)) {
            userStorage.deleteFriend(userId, friendId);
            userServiceLogger.log(Level.INFO, "successfully /friends DELETE");
            return userStorage.getUser(userId);
        } else {
            userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends DELETE");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    public List<User> getAllFriendsOfUser(long userId) {
        if (userStorage.containsUser(userId)) {
            userServiceLogger.log(Level.INFO, "successfully /allfriends");
            return userStorage.getAllFriendsOfUser(userId);
        } else {
            userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /allfriends");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    public List<User> findCommonFriends(long userId, long anotherId) {
        if (userStorage.containsUser(userId) && userStorage.containsUser(anotherId)) {
            userServiceLogger.log(Level.INFO, "successfully /friends/common");
            return userStorage.findCommonFriends(userId, anotherId);
        } else {
            userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /friends/common");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }
}

