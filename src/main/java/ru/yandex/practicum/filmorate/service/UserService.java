package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

   public void addFriend(User user, User friend) {
        user.getListOfFriends().add(friend);
        friend.getListOfFriends().add(user);
    }

    public void deleteFriend(User user, User friend) {
       user.getListOfFriends().remove(friend);
       friend.getListOfFriends().remove(user);
    }

    public Set<User> getAllFriendsOfUser(User user) {
      return user.getListOfFriends();
    }

    public List<User> findCommonFriends(User user, User otherUser) {
        Set<User> listOfFriends = user.getListOfFriends();
        Set<User> listOfFriends1 =  otherUser.getListOfFriends();
        List<User> commonFriends = listOfFriends.stream().filter(listOfFriends1::contains).collect(toList());
        return commonFriends;
    }
}
