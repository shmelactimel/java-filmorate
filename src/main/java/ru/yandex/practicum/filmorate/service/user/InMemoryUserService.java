package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;

import java.util.List;

@Service
public class InMemoryUserService implements UserService {
    @Autowired
    private UserStorage userStorage;

    @Override
    public List<Long> addFriend(long userId1, long userId2) throws ValidationException {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.addFriend(userId2);
        user2.addFriend(userId1);
        return new ArrayList<>(user1.getFriends());
    }

    @Override
    public List<Long> deleteFriend(long userId1, long userId2) throws ValidationException {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.removeFriend(userId2);
        user2.removeFriend(userId1);
        return new ArrayList<>(user1.getFriends());
    }

    @Override
    public List<User> getMutualFriends(long userId1, long userId2) throws ValidationException {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        List<User> result = new ArrayList<>();
        for (Long user1Friend : user1.getFriends()) {
            for (Long user2FriendId : user2.getFriends()) {
                if (user2FriendId == user1Friend) {
                    User mutualFriend = userStorage.getUserById(user2FriendId);
                    result.add(mutualFriend);
                }
            }
        }
        return result;
    }

    @Override
    public List<User> getUsersFriends(long userId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;
    }
}
