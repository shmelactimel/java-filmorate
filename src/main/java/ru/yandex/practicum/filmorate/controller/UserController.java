package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private long maxId;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        user.setId(generateId());
        if (Optional.ofNullable(user.getName()).isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getLogin());
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        long userId = user.getId();
        if (users.containsKey(userId)) {
            User currentUser = users.get(userId);
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setLogin(user.getLogin());
            currentUser.setBirthday(user.getBirthday());
            log.info("Информация о пользователе обновлена");
            return currentUser;
        } else {
            log.warn("Данные пользователя не были обновлены");
            throw new ValidationException("Ошибка при обновлении информации о пользователе");
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> resultList = new ArrayList<>(users.values());
        log.info("Клиент получил список пользователей");
        return resultList;
    }

    private long generateId() {
        return ++maxId;
    }
}
