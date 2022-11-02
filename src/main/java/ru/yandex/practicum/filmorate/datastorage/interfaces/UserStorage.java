package ru.yandex.practicum.filmorate.datastorage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;



public interface UserStorage {

    User addUser(User user);

    boolean containsUser(long userId);

    List<User> getAllUsers();

    User getUser(long id);

    void updateUser(User user);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getAllFriendsOfUser(long userId);

    List<User> findCommonFriends(long userId, long anotherUserId);
}
