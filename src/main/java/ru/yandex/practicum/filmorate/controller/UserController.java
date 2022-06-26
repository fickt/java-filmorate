package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping //newuser
    public ResponseEntity<User> createNewUser(@RequestBody User user) {

        userService.newUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping //updateuser
    public ResponseEntity<User> updateUser(@RequestBody User user) {

        userService.updateUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping //allusers
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}") //finduser
    public ResponseEntity<User> getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {

       return userService.addFriend(id, friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable long id, @PathVariable long friendId) {

       return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<Long>> getAllFriendsOfUser(@PathVariable long id) {

       return userService.getAllFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<Long>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {

       return userService.findCommonFriends(id, otherId);
    }
}
