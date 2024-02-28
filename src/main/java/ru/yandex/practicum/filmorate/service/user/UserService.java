package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


public interface UserService {
    public List<Long> addFriend(long userId1, long userId2) throws ValidationException;

    public List<Long> deleteFriend(long userId1, long userId2) throws ValidationException;

    public List<User> getMutualFriends(long userId1, long userId2) throws ValidationException;

    public List<User> getUsersFriends(long userId) throws ValidationException;
}
