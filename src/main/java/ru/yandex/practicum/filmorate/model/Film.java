package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.MinDate;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;

    @NotBlank(message = "Название фильма не может быть пустым и не существовать")
    private String name;

    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private String description;

    @MinDate
    @NotNull(message = "Дата выхода не может быть ранее 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "Длительность фильма не может быть отрицательной или равняться 0")
    private long duration;

    private Set<Long> usersLike = new HashSet<>();

    public Set<Long> addLike(Long userId) {
        usersLike.add(userId);
        return usersLike;
    }

    public Set<Long> deleteLike(Long userId) {
        usersLike.remove(userId);
        return usersLike;
    }
}
