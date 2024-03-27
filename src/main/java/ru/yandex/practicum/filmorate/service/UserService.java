package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) throws EntityAlreadyExistException {
        return userStorage.addUser(user);
    }

    public User updateUser(Long userId, User user) {
        return userStorage.updateUser(userId, user);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public void approveFriend(Long id, Long friendId) {
        userStorage.approveFriend(id, friendId);
    }

    public static Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());
        return values;
    }
}