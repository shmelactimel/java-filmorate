package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @DeleteMapping("/films")
    public Film deleteFilm(long filmId) {
        return filmService.deleteFilm(filmId);
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film setLike(@PathVariable Long filmId, @PathVariable Long userId) {
        Film film = filmService.getFilmById(filmId);
        filmService.addLike(filmId, userId);
        return film;
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        Film film = filmService.getFilmById(filmId);
        filmService.deleteLike(filmId, userId);
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> getTopRatedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopRatedFilms(count);
    }
}
