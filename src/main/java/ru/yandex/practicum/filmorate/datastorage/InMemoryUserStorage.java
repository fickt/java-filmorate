package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID

    private List<String> logins = new ArrayList<>();

    public void addUser (long userId, User user) {
        userStorage.put(userId, user);
        logins.add(user.getLogin());
    }

    public boolean containsUser(long userId) {
        return userStorage.containsKey(userId);
    }


    public boolean containsUser(String login) {
       return logins.contains(login);
    }

    public Map<Long, User> getAllUsers() {
        return userStorage;
    }

   public  List<String> getLogins() {
        return logins;
   }
}
