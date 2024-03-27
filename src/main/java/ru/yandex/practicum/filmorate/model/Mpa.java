package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Mpa {
    private Long id;
    @NotBlank(message = "name cannot be blank")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "no special characters or space allowed.")
    private String name;

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}