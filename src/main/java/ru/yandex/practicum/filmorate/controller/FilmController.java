package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import javax.validation.Valid;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("get all films");
        return filmService.getFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable(required = true) Long id) {
        log.info("get film id :" + id);
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws EntityAlreadyExistException {
        log.info("add new film:" + film.getName());
        return filmService.addFilm(film);
    }

    @PutMapping(value = {"","/{filmId}"})
    public Film updateFilm(@PathVariable(required = false) Long filmId, @Valid @RequestBody Film film) {
        return filmService.updateFilm(filmId, film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        log.info("add like to movie id:" + id + " from user id:" + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.info("remove like from movie id:" + id + " from user id:" + userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("get top " + count + " movies");
        return filmService.getTopFilms(count);
    }
}