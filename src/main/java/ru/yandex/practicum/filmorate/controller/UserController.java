package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

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

        return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
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
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {

       return new ResponseEntity<>(userService.addFriend(id, friendId), HttpStatus.OK);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
       return new ResponseEntity<>( userService.deleteFriend(id, friendId), HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getAllFriendsOfUser(@PathVariable long id) {

       return new ResponseEntity<>(userService.getAllFriendsOfUser(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {

       return new ResponseEntity<>(userService.findCommonFriends(id, otherId),HttpStatus.OK);
    }
}
