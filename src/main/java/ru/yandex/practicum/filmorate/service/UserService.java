package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

   public void addFriend(User user, User friend) {
        user.getListOfFriends().add(friend.getId());
        friend.getListOfFriends().add(user.getId());
    }

    public void deleteFriend(User user, User friend) {
       user.getListOfFriends().remove(friend);
       friend.getListOfFriends().remove(user);
    }

    public Set<Long> getAllFriendsOfUser(User user) {
      return user.getListOfFriends();
    }

    public Set<Long> findCommonFriends(User user, User otherUser) {
        Set<Long> listOfFriends = user.getListOfFriends();
        Set<Long> listOfFriends1 =  otherUser.getListOfFriends();
        Set<Long> commonFriends = listOfFriends.stream().filter(listOfFriends1::contains).collect(Collectors.toSet());
        return commonFriends;
    }
}
