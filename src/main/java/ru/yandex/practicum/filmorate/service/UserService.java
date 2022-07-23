package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.UserDbStorage;
import ru.yandex.practicum.filmorate.datastorage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.datastorage.rowmappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class UserService {

    private UserStorage userStorage;
    private Logger userServiceLogger = Logger.getLogger("userServiceLogger");
    private long userIdGenerator = 1;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User newUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            userServiceLogger.log(Level.WARNING, "ValidationException /newuser");
            throw new ValidationException("Ошибка валидации /user/newuser");
        } else {

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            user.setId(userIdGenerator);
            userStorage.addUser(userIdGenerator, user);
            userIdGenerator++;
            userServiceLogger.log(Level.INFO, "user has been created! /newuser");
        }
        return user;
    }

    public User updateUser(User user) {
        if (!(userStorage instanceof UserDbStorage)) {
            if (userStorage.containsUser(user.getId())) {
                userStorage.addUser(user.getId(), user);
                userServiceLogger.log(Level.INFO, "user has been updated! /updateuser");
                return user;
            } else {
                Logger.getLogger("logger").log(Level.WARNING, "ValidationException /updateuser " + user.getId());
                throw new NotFoundException("Ошибка валидации user/updateuser");
            }
        } else {
            if (userStorage.containsUser(user.getId())) {
                ((UserDbStorage) userStorage).updateUser(user);
                return userStorage.getUser(user.getId());
            } else {
                throw new NotFoundException("Ошибка валидации user/updateuser");
            }
        }

    }

    public java.util.Collection<User> getAllUsers() {
        userServiceLogger.log(Level.INFO, " /allusers");
        return userStorage.getAllUsers();
    }

    public ResponseEntity<User> getUser(long userId) {
        userServiceLogger.log(Level.INFO, "/finduser");

        if (userStorage.containsUser(userId)) {
            userServiceLogger.log(Level.INFO, "successfully /finduser");
            return new ResponseEntity<>(userStorage.getUser(userId), HttpStatus.OK);
        } else {
            userServiceLogger.log(Level.WARNING, "User with " + userId + " ID hasn't been found! /finduser ");
            throw new NotFoundException("User with given ID hasn't been found!");
        }

    }

    public ResponseEntity<User> addFriend(long id, long friendId) {

        if (!(userStorage instanceof UserDbStorage)) {
            if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
                userStorage.getUser(id).getListOfFriends().add(friendId);
                userStorage.getUser(friendId).getListOfFriends().add(id);
                userServiceLogger.log(Level.INFO, "successfully /friends");
                return new ResponseEntity<>(userStorage.getUser(id), HttpStatus.OK);
            } else {
                userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends");
                throw new NotFoundException("User/users with given ID has/haven't been found!");
            }
        } else { // если реализация UserDbStorage
            if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
                ((UserDbStorage) userStorage).getJdbcTemplate().update("INSERT INTO FRIENDS_TABLE (USER_ID, ANOTHER_USER_ID) VALUES (?,?)", id, friendId);
                return new ResponseEntity<>(userStorage.getUser(id), HttpStatus.OK);
            } else {
                throw new NotFoundException("User/users with given ID has/haven't been found!");
            }
        }
    }

    public ResponseEntity<User> deleteFriend(long id, long friendId) {
        if (!(userStorage instanceof UserDbStorage)) {
            if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
                userStorage.getUser(id).getListOfFriends().remove(friendId);
                userStorage.getUser(friendId).getListOfFriends().remove(id);
                userServiceLogger.log(Level.INFO, "successfully /friends DELETE");
                return new ResponseEntity<>(userStorage.getUser(id), HttpStatus.OK);
            } else {
                userServiceLogger.log(Level.WARNING, "User with given ID hasn't been found! /friends DELETE");
                throw new NotFoundException("User/users with given ID has/haven't been found!");
            }
        } else {// если реализация UserDbStorage
            ((UserDbStorage) userStorage).getJdbcTemplate().update("DELETE FROM FRIENDS_TABLE WHERE USER_ID = ? AND ANOTHER_USER_ID = ?", id, friendId);
            return new ResponseEntity<>(userStorage.getUser(id), HttpStatus.OK);
        }
    }

    public ResponseEntity<List<User>> getAllFriendsOfUser(long id) {
        if (!(userStorage instanceof UserDbStorage)) {
            if (userStorage.containsUser(id)) {
                userServiceLogger.log(Level.INFO, "successfully /allfriends");
                Set<Long> userFriends = userStorage.getUser(id).getListOfFriends();
                List<User> listOfUserFriends = userStorage.getAllUsers().stream()
                        .filter(o -> userFriends.contains(o.getId()))
                        .collect(Collectors.toList());

                return new ResponseEntity(listOfUserFriends, HttpStatus.OK);
            } else {
                userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /allfriends");
                throw new NotFoundException("User/users with given ID has/haven't been found!");
            }
        } else {
            List<Map<String, Object>> allFriendsOfUser = ((UserDbStorage) userStorage).getJdbcTemplate()
                    .queryForList("SELECT * FROM FRIENDS_TABLE " +
                            "LEFT JOIN USER_TABLE " +
                            "ON FRIENDS_TABLE.ANOTHER_USER_ID = USER_TABLE.ID " +
                            "WHERE FRIENDS_TABLE.USER_ID =?", id);
            return new ResponseEntity(UserRowMapper.userMapper(allFriendsOfUser), HttpStatus.OK);
        }
    }


    public ResponseEntity<List<User>> findCommonFriends(long id, long otherId) {
        if (!(userStorage instanceof UserDbStorage)) {
            if (userStorage.containsUser(id) && userStorage.containsUser(otherId)) {
                userServiceLogger.log(Level.INFO, "successfully /friends/common");

                Set<Long> listOfFriends = userStorage.getUser(id).getListOfFriends();
                Set<Long> listOfFriends1 = userStorage.getUser(otherId).getListOfFriends();
                Set<Long> commonFriends = listOfFriends
                        .stream()
                        .filter(listOfFriends1::contains)
                        .collect(Collectors.toSet());

                List<User> listOfCommonFriends = userStorage.getAllUsers()
                        .stream()
                        .filter(o -> commonFriends
                                .contains(o.getId()))
                        .collect(Collectors.toList());

                return new ResponseEntity<>(listOfCommonFriends, HttpStatus.OK);
            } else {
                userServiceLogger.log(Level.INFO, "User with given ID hasn't been found! /friends/common");
                throw new NotFoundException("User/users with given ID has/haven't been found!");
            }
        } else {
            List<Map<String, Object>> allFriendsOfFirstUser = ((UserDbStorage) userStorage).getJdbcTemplate()
                    .queryForList("SELECT * FROM FRIENDS_TABLE " +
                            "LEFT JOIN USER_TABLE " +
                            "ON FRIENDS_TABLE.ANOTHER_USER_ID = USER_TABLE.ID " +
                            "WHERE FRIENDS_TABLE.USER_ID =?", id);

            List<Map<String, Object>> allFriendsOfSecondUser = ((UserDbStorage) userStorage).getJdbcTemplate()
                    .queryForList("SELECT * FROM FRIENDS_TABLE " +
                            "LEFT JOIN USER_TABLE " +
                            "ON FRIENDS_TABLE.ANOTHER_USER_ID = USER_TABLE.ID " +
                            "WHERE FRIENDS_TABLE.USER_ID =?", otherId);

            List<User> allFriendsOfFirstUserAsList = UserRowMapper.userMapper(allFriendsOfFirstUser);
            List<User> allFriendsOfSecondUserAsList = UserRowMapper.userMapper(allFriendsOfSecondUser);

            List<User> commonFriends = allFriendsOfFirstUserAsList
                    .stream()
                    .filter(allFriendsOfSecondUserAsList::contains)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(commonFriends, HttpStatus.OK);
        }
    }
}
