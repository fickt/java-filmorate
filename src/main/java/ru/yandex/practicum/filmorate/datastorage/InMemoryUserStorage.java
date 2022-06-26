package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;



@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    @PostMapping
    public void addUser (long userId, User user) {
        userStorage.put(userId, user);

    }

    public boolean containsUser(long userId) {
        return userStorage.containsKey(userId);
    }


    public Map<Long, User> getAllUsers() {
        return userStorage;
    }

}
