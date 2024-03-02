package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
    private long maxId;

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(long filmId) {
        Film filmToDelete = films.remove(filmId);
        return filmToDelete;
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException {
        long filmId = film.getId();
        if (films.containsKey(filmId)) {
            Film currentFilm = films.get(filmId);
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            return currentFilm;
        } else {
            throw new FilmNotFoundException("Ошибка при обновлении информации о фильме");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> resultList = new ArrayList<>(films.values());
        return resultList;
    }

    @Override
    public Film getFilmById(long filmId) throws FilmNotFoundException {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + "не найден");
        }
        return films.get(filmId);
    }

    private long generateId() {
        return ++maxId;
    }
}