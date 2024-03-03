package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(long userId1, long userId2);

    void deleteFriend(long userId1, long userId2);

    List<User> getMutualFriends(long userId1, long userId2);

    List<User> getUsersFriends(long userId);

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User deleteUser(long userId);

    User getUserById(long userId);
}
