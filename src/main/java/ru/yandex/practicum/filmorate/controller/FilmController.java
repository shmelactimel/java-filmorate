package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private HashMap<Long, Film> films = new HashMap<>();
    private long maxId;

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        long filmId = film.getId();
        if (films.containsKey(filmId)) {
            Film currentFilm = films.get(filmId);
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            log.info("Информация о фильме обновлена");
            return currentFilm;
        } else {
            log.warn("Информация о фильме не была обновлена");
            throw new ValidationException("Ошибка при обновлении информации о фильме");
        }
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> resultList = new ArrayList<>(films.values());
        log.info("Клиент получил список фильмов");
        return resultList;
    }

    private long generateId() {
        return ++maxId;
    }
}
