package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;



public interface UserStorage {

    void addUser(long userId, User user);

    boolean containsUser(long userId);

    boolean containsUser(String login);

    Map<Long, User> getAllUsers();

    public List<String> getLogins();

}
