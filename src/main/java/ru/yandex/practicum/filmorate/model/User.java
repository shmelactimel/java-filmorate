package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;

    @NotBlank(message = "Электронная почта не существует или пустая")
    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @NotBlank(message = "Логин не существует или пуст")
    @Pattern(regexp = "\\S+", message = "Логин содержит пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не существует")
    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public Set<Long> addFriend(Long friendId) {
        friends.add(friendId);
        return friends;
    }

    public Set<Long> removeFriend(Long friendId) {
        friends.remove(friendId);
        return friends;
    }
}
