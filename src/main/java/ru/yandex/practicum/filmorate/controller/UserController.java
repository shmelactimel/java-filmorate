package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        userStorage.addUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        userStorage.updateUser(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @DeleteMapping("/users")
    public User deleteUser(long userId) {
        User userToDelete = userStorage.deleteUser(userId);
        return userToDelete;
    }

    @GetMapping("users/{userId}")
    public User getUserById(@PathVariable long userId) throws ValidationException {
        return userStorage.getUserById(userId);
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public List<Long> addFriend(@PathVariable long userId,
                                @PathVariable long friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        userService.addFriend(userId, friendId);
        return new ArrayList<>(user.getFriends());
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public List<Long> deleteFriend(@PathVariable long userId,
                                   @PathVariable long friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        userService.deleteFriend(userId, friendId);
        return new ArrayList<>(user.getFriends());
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getAllUsersFriends(@PathVariable Long userId) throws ValidationException {
        return userService.getUsersFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable Long userId,
                                       @PathVariable Long friendId) throws ValidationException {
        return userService.getMutualFriends(userId, friendId);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownUserException(final NullPointerException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка", "Пользователь не найден"),
                HttpStatus.NOT_FOUND
        );
    }
}
