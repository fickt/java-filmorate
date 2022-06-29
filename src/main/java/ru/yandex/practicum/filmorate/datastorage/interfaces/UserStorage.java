package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;


public interface UserStorage {

    void addUser(long userId, User user);

    boolean containsUser(long userId);

    Map<Long, User> getAllUsers();


}
