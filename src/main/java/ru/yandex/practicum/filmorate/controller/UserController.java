package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        userService.updateUser(user);
        return user;
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
        return userService.getUserById(userId);
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId,
                          @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId,
                             @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getAllUsersFriends(@PathVariable Long userId) {
        return userService.getUsersFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable Long userId,
                                       @PathVariable Long friendId) {
        return userService.getMutualFriends(userId, friendId);
    }
}