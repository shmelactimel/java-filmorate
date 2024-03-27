package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import ru.yandex.practicum.filmorate.utils.CustomEmailValidator;

@Data
public class User {
    private Long id;
    @CustomEmailValidator
    private String email;
    @NotBlank(message = "login cannot be blank")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "no special characters or space allowed.")
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}