package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> mapOfFilms = new HashMap<>();
    private Long id = 0L;

    public List<Film> getFilms() {
        return new ArrayList<>(mapOfFilms.values());
    }

    public Film getFilm(Long filmId) {
        Film film = mapOfFilms.get(filmId);
        if (film == null) {
            throw new EntityNotFoundException("film not found");
        }
        return film;
    }

    public Film addFilm(Film film) throws EntityAlreadyExistException {
        if ((film.getId() != null) && (mapOfFilms.get(film.getId()) != null)) {
            throw new EntityAlreadyExistException("film id:" + film.getId() + "already exists");
        }
        film.setId(++id);
        mapOfFilms.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Long filmId, Film film) {
        if (filmId == null) {
            filmId = film.getId();
        }
        log.info("update film id: " + filmId);
        if (filmId == null || (mapOfFilms.get(filmId) == null)) {
            throw new EntityNotFoundException("film not found");
        }
        film.setId(filmId);
        mapOfFilms.put(film.getId(), film);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = mapOfFilms.get(filmId);
        if (film == null) {
            throw new EntityNotFoundException("film not found");
        }
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = mapOfFilms.get(filmId);
        if (film == null) {
            throw new EntityNotFoundException("film not found");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getTopFilms(Integer count) {
        return new ArrayList<>(getFilms().stream().sorted(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikesCount() - o1.getLikesCount();
            }
        }).limit(count).collect(Collectors.toSet()));
    }
}