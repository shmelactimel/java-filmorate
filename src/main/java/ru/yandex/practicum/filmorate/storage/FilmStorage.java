package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film) throws EntityAlreadyExistException;

    Film updateFilm(Long filmId, Film film);

    Film getFilm(Long filmId);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getTopFilms(Integer count);
}