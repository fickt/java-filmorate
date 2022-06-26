package ru.yandex.practicum.filmorate.datastorage.interfaces;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;


@Component
public interface UserStorage {

    void addUser(long userId, User user);

    boolean containsUser(long userId);

    Map<Long, User> getAllUsers();


}
