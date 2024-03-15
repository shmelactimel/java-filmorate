package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {
    @Autowired
    private UserStorage userStorage;

    @Override
    public void addFriend(long userId1, long userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.addFriend(userId2);
        user2.addFriend(userId1);
    }

    @Override
    public void deleteFriend(long userId1, long userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.removeFriend(userId2);
        user2.removeFriend(userId1);
    }

    @Override
    public List<User> getMutualFriends(long userId1, long userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);

        // Создаем копии списков друзей для избежания изменения исходных списков
        Set<Long> user1FriendsSet = new HashSet<>(user1.getFriends());
        Set<Long> user2FriendsSet = new HashSet<>(user2.getFriends());

        // Оставляем только общих друзей, используя метод retainAll()
        user1FriendsSet.retainAll(user2FriendsSet);

        // Создаем список общих друзей
        List<User> mutualFriends = new ArrayList<>();
        for (Long friendId : user1FriendsSet) {
            User mutualFriend = userStorage.getUserById(friendId);
            mutualFriends.add(mutualFriend);
        }

        return mutualFriends;
    }

    @Override
    public List<User> getUsersFriends(long userId) {
        User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User deleteUser(long userId) {
        return userStorage.deleteUser(userId);
    }

    @Override
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }
}