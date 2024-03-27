package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("get all genres");
        return genreService.getGenres();
    }

    @GetMapping(value = "/{id}")
    public Genre getGenre(@PathVariable(required = true) Long id) {
        log.info("get genre id :" + id);
        return genreService.getGenre(id);
    }
}