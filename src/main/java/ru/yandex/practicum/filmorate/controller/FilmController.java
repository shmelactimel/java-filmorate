package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        filmStorage.updateFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @DeleteMapping("/films")
    public Film deleteFilm(long filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film setLike(@PathVariable Long filmId, @PathVariable Long userId) throws ValidationException {
        Film film = filmStorage.getFilmById(filmId);
        filmService.addLike(filmId, userId);
        return film;
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Long filmId, @PathVariable Long userId) throws ValidationException {
        Film film = filmStorage.getFilmById(filmId);
        filmService.deleteLike(filmId, userId);
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> getTopRatedFilms(@RequestParam(required = false) Integer count) {
        int defaultCount = 10;
        if (Optional.ofNullable(count).isPresent()) {
            System.out.println(filmService.getTopRatedFilms().subList(0, count));
            return filmService.getTopRatedFilms().subList(0, count);
        } else {
            if (filmStorage.getAllFilms().size() < defaultCount) {
                return filmService.getTopRatedFilms().subList(0, filmService.getTopRatedFilms().size());
            } else {
                return filmService.getTopRatedFilms().subList(0, defaultCount);
            }
        }
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownFilmException(final NullPointerException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка", "Фильм не найден"),
                HttpStatus.NOT_FOUND
        );
    }
}

