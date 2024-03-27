package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("get all users");
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws EntityAlreadyExistException {
        log.info("add new user:" + user.getLogin());
        return userService.addUser(user);
    }

    @PutMapping(value = {"","/{userId}"})
    public User updateUser(@PathVariable(required = false) Long userId, @Valid @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("get user id :" + id);
        return userService.getUser(id);
    }

    @GetMapping(value = "/{id}/friends")
    public Set<User> getUserFriends(@PathVariable Long id) {
        log.info("get user id :" + id + " friends");
        return userService.getFriends(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.info("add friend id :" + friendId + " to user id :" + id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        log.info("remove friend id :" + friendId + " from user id :" + id);
        userService.removeFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long friendId) {
        log.info("get common friends between user id :" + id + " and friend id :" + friendId);
        return userService.getCommonFriends(id, friendId);
    }

    @PutMapping(value = "/{id}/friends/common/{friendId}")
    public void approveFriend(@PathVariable Long id,
                              @PathVariable Long friendId) {
        log.info("approve friend id : " + friendId + " for user id :" + id);
        userService.approveFriend(id, friendId);
    }
}