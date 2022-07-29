package ru.yandex.practicum.filmorate.datastorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userStorage = new HashMap<>(); // Using HashMaps to facilitate the process of searching users by ID
    private long userIdGenerator = 1;

    @Override
    public User addUser(User user) {
        user.setId(userIdGenerator);
        userStorage.put(userIdGenerator, user);
        userIdGenerator++;
        return userStorage.get(userIdGenerator - 1);
    }

    @Override
    public boolean containsUser(long userId) {
        return userStorage.containsKey(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userStorage.values();
    }

    @Override
    public User getUser(long id) {
        return userStorage.get(id);
    }

    @Override
    public void updateUser(User user) {
        userStorage.put(user.getId(), user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userStorage.get(userId).getListOfFriends().add(friendId);
        userStorage.get(friendId).getListOfFriends().add(userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        userStorage.get(userId).getListOfFriends().remove(friendId);
        userStorage.get(friendId).getListOfFriends().remove(userId);
    }

    @Override
    public List<User> getAllFriendsOfUser(long userId) {
        Set<Long> userFriends = userStorage.get(userId).getListOfFriends();
        return getAllUsers()
                .stream()
                .filter(o -> userFriends.contains(o.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends (long userId, long anotherId) {
        Set<Long> listOfFriends = userStorage.get(userId).getListOfFriends();
        Set<Long> listOfFriends1 = userStorage.get(anotherId).getListOfFriends();

        Set<Long> commonFriends = listOfFriends
                .stream()
                .filter(listOfFriends1::contains)
                .collect(Collectors.toSet());

        return getAllUsers()
                .stream()
                .filter(o -> commonFriends
                        .contains(o.getId()))
                .collect(Collectors.toList());
    }

}
