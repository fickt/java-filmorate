package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;



public interface UserStorage {

    void addUser(long userId, User user);

    boolean containsUser(long userId);

    List<User> getAllUsers();

    User getUser(long id);

}
