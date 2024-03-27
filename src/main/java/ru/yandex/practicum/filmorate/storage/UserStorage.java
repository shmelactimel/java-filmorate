package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    List<User> getUsers();

    User getUser(Long userId);

    User addUser(User user) throws EntityAlreadyExistException;

    User updateUser(Long userId, User user);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long userId, Long friendId);

    Set<User> getFriends(Long userId);

    void approveFriend(Long id, Long friendId);
}