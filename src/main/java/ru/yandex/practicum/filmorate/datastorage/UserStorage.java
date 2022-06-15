package ru.yandex.practicum.filmorate.datastorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private Map<Integer, User> userStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    public void addUser (int userId, User user) {
        userStorage.put(userId, user);
    }

    public boolean containsUser(int userId) {
        return userStorage.containsKey(userId);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage;
    }
}
