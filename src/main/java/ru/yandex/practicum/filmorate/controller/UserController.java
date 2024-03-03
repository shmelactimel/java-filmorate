package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return user;
        } catch (UserNotFoundException e) {
            log.error("Ошибка при обновлении пользователя: " + e.getMessage());
            throw new RuntimeException("Ошибка при обновлении пользователя: " + e.getMessage(), e);
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users")
    public void deleteUser(long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("users/{userId}")
    public User getUserById(@PathVariable long userId) {
        try {
            return userService.getUserById(userId);
        } catch (UserNotFoundException e) {
            log.error("Ошибка при получении пользователя: " + e.getMessage());
            throw new RuntimeException("Ошибка при получении пользователя: " + e.getMessage(), e);
        }
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId,
                          @PathVariable long friendId) {
        try {
            userService.addFriend(userId, friendId);
        } catch (UserNotFoundException e) {
            log.error("Ошибка при добавлении друга: " + e.getMessage());
            throw new RuntimeException("Ошибка при добавлении друга: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId,
                             @PathVariable long friendId) {
        try {
            userService.deleteFriend(userId, friendId);
        } catch (UserNotFoundException e) {
            log.error("Ошибка при удалении друга: " + e.getMessage());
            throw new RuntimeException("Ошибка при удалении друга: " + e.getMessage(), e);
        }
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getAllUsersFriends(@PathVariable Long userId) {
        try {
            return userService.getUsersFriends(userId);
        } catch (UserNotFoundException e) {
            log.error("Ошибка при получении друзей пользователя: " + e.getMessage());
            throw new RuntimeException("Ошибка при получении друзей пользователя: " + e.getMessage(), e);
        }
    }

    @GetMapping("/users/{userId}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable Long userId,
                                       @PathVariable Long friendId) {
        try {
            return userService.getMutualFriends(userId, friendId);
        } catch (UserNotFoundException e) {
            log.error("Ошибка при получении общих друзей: " + e.getMessage());
            throw new RuntimeException("Ошибка при получении общих друзей: " + e.getMessage(), e);
        }
    }
}