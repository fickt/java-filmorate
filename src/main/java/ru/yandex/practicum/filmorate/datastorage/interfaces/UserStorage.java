package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;


public interface UserStorage {

    void addUser (int userId, User user);

    boolean containsUser(int userId);

    Map<Integer, User> getAllUsers();
}
