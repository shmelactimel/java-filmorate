package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

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
}
