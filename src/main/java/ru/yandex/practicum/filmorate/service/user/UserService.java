package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public void addFriend(long userId1, long userId2) throws UserNotFoundException;

    public void deleteFriend(long userId1, long userId2) throws UserNotFoundException;

    public List<User> getMutualFriends(long userId1, long userId2) throws UserNotFoundException;

    public List<User> getUsersFriends(long userId) throws UserNotFoundException;

    public User addUser(User user);

    public User updateUser(User user) throws UserNotFoundException;

    public List<User> getAllUsers();

    public User deleteUser(long userId);

    public User getUserById(long userId) throws UserNotFoundException;
}
