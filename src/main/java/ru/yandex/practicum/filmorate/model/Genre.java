package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Genre {
    private Long id;
    @NotBlank(message = "name cannot be blank")
    private String name;

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}