package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private long maxId;

    @Override
    public User addUser(User user) {
        user.setId(generateId());
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(long userId) {
        User userToDelete = users.remove(userId);
        return userToDelete;
    }

    @Override
    public User updateUser(User user) {
        long userId = user.getId();
        if (users.containsKey(userId)) {
            User currentUser = users.get(userId);
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setLogin(user.getLogin());
            currentUser.setBirthday(user.getBirthday());
            return currentUser;
        } else {
            throw new UserNotFoundException("Пользователь(-и) не найден(-ы)");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> resultList = new ArrayList<>(users.values());
        return resultList;
    }

    @Override
    public User getUserById(long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user;
    }

    private long generateId() {
        return ++maxId;
    }
}
