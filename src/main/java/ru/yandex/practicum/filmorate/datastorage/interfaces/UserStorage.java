package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

<<<<<<< Updated upstream

public interface UserStorage {

    void addUser (int userId, User user);

    boolean containsUser(int userId);

    Map<Integer, User> getAllUsers();
=======
public interface UserStorage {

    public void addUser (int userId, User user);

    public boolean containsUser(int userId);

    public Map<Integer, User> getAllUsers();
>>>>>>> Stashed changes
}
