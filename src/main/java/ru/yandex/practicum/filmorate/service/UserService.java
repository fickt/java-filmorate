package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserStorage userStorage;
    private Logger userServiceLogger = Logger.getLogger("userServiceLogger");
    long userIdGenerator = 1;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User newUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            userServiceLogger.log(Level.WARNING, "ValidationException /newuser");
            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            user.setId(userIdGenerator);
            userStorage.addUser(userIdGenerator, user);
            userIdGenerator++;
            userServiceLogger.log(Level.INFO, "user has been created! /newuser");
        }
        return user;
    }

    public User updateUser(User user) {

        if (userStorage.containsUser(user.getId())) {
            userStorage.addUser(user.getId(), user);
            userServiceLogger.log(Level.INFO, "user has been updated! /updateuser");
            return user;
        } else {
            Logger.getLogger("logger").log(Level.WARNING, "ValidationException /updateuser " + user.getId());
            throw new NotFoundException("Ошибка валидации user/updateuser");
        }

    }

    public java.util.Collection<User> getAllUsers() {
        userServiceLogger.log(Level.INFO, " /allusers");
        return userStorage.getAllUsers().values();
    }

    public ResponseEntity<User> getUser(long userId) {
        userServiceLogger.log(Level.INFO, "/finduser");

        if (userStorage.getAllUsers().containsKey(userId)) {
            userServiceLogger.log(Level.INFO, "successfully /finduser");
            return new ResponseEntity<>(userStorage.getAllUsers().get(userId), HttpStatus.OK);
        } else {
            userServiceLogger.log(Level.WARNING, "User with " + userId + " ID hasn't been found! /finduser ");
            throw new NotFoundException("User with given ID hasn't been found!");
        }

    }

   public ResponseEntity<User> addFriend(long id, long friendId) {


       if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
           userStorage.getAllUsers().get(id).getListOfFriends().add(friendId);
           userStorage.getAllUsers().get(friendId).getListOfFriends().add(id);
           userServiceLogger.log(Level.INFO, "successfully /friends");
           return new ResponseEntity<>(userStorage.getAllUsers().get(id), HttpStatus.OK);
       } else {
           userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends");
           throw new NotFoundException("User/users with given ID has/haven't been found!");
       }
    }

    public ResponseEntity<User> deleteFriend(long id, long friendId) {

        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            userStorage.getAllUsers().get(id).getListOfFriends().remove(friendId);
            userStorage.getAllUsers().get(friendId).getListOfFriends().remove(id);
            userServiceLogger.log(Level.INFO, "successfully /friends DELETE");
            return new ResponseEntity<>(userStorage.getAllUsers().get(id), HttpStatus.OK);
        } else {
            userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends DELETE");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    public ResponseEntity<Set<Long>> getAllFriendsOfUser(long id) {
        if (userStorage.containsUser(id)) {
            userServiceLogger.log(Level.INFO, "successfully /allfriends");
            return new ResponseEntity(userStorage.getAllUsers().get(id).getListOfFriends(), HttpStatus.OK);
        } else {
            userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /allfriends");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }

    public ResponseEntity<Set<Long>> findCommonFriends(long id, long otherId) {

        if (userStorage.containsUser(id) && userStorage.containsUser(otherId)) {
            userServiceLogger.log(Level.INFO, "successfully /friends/common");
            Set<Long> listOfFriends = userStorage.getAllUsers().get(id).getListOfFriends();
            Set<Long> listOfFriends1 =  userStorage.getAllUsers().get(otherId).getListOfFriends();
            Set<Long> commonFriends = listOfFriends.stream().filter(listOfFriends1::contains).collect(Collectors.toSet());

            return new ResponseEntity<>(commonFriends, HttpStatus.OK);
        } else {
            userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /friends/common");
            throw new NotFoundException("User/users with given ID has/haven't been found!");
        }
    }
}
